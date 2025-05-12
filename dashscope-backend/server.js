import express from 'express';
import OpenAI from 'openai';
import * as dotenv from 'dotenv';
import os from 'node:os'; // 用于获取本机IP

// 加载环境变量
dotenv.config();


function getServerIP() {
    const interfaces = os.networkInterfaces();

    let ethernetIP = null;
    let wifiIP = null;

    for (const name of Object.keys(interfaces)) {
        for (const intf of interfaces[name]) {
            if (
                intf.family === 'IPv4' &&
                !intf.internal // 排除回环地址
            ) {
                // 排除虚拟机和虚拟网卡
                if (
                    name.includes('VMware') ||
                    name.includes('vEthernet') ||
                    name.includes('Docker') ||
                    name.includes('Loopback')
                ) {
                    continue;
                }

                // 判断是否是以太网（包含 "以太网" 或 "Ethernet"）
                const isEthernet =
                    name.includes('以太网') || name.toLowerCase().includes('ethernet');

                // 判断是否是无线网络（包含 "WLAN" 或 "Wi-Fi" 或 "WiFi"）
                const isWiFi =
                    name.includes('WLAN') ||
                    name.toLowerCase().includes('wi-fi') ||
                    name.toLowerCase().includes('wifi');

                // 只记录第一个有效 IP（避免重复）
                if (isEthernet && !ethernetIP) {
                    ethernetIP = intf.address;
                } else if (isWiFi && !wifiIP) {
                    wifiIP = intf.address;
                }
            }
        }
    }

    return ethernetIP || wifiIP || 'localhost';
}


// 初始化 OpenAI 客户端
const openai = new OpenAI({
    apiKey: process.env.DASHSCOPE_API_KEY,
    baseURL: 'https://dashscope.aliyuncs.com/compatible-mode/v1'
});

// 创建 Express 应用
const app = express();
app.use(express.json());

// 中间件：记录客户端IP和服务端IP
app.use((req, res, next) => {
    const clientIP = req.ip || req.socket?.remoteAddress || 'unknown';
    const serverIP = getServerIP();
    const date = new Date().toISOString();
    console.log(`[${date}] Request from ${clientIP} to ${serverIP}${req.url}`);
    next();
});

// 定义 API 路由
app.post('/chat', async (req, res) => {
    const { message } = req.body;

    if (!message) {
        return res.status(400).json({ error: 'Missing "message" in request body' });
    }

    try {
        const stream = await openai.chat.completions.create({
            model: 'qwq-32b',
            messages: [{ role: 'user', content: message }],
            stream: true
        });

        let fullResponse = ''; // 用于存储完整的响应内容

        for await (const chunk of stream) {
            if (!chunk.choices?.length) continue;
            const delta = chunk.choices[0].delta;
            if (delta.reasoning_content || delta.content) {
                const content = delta.reasoning_content || delta.content;
                fullResponse += content; // 拼接内容
            }
        }

        console.log('responseJson:', fullResponse);
        res.json({ response: fullResponse });
    } catch (error) {
        console.error('Error:', error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

// 启动服务器
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    const serverIP = getServerIP();
    console.log(`Server is running on http://${serverIP}:${PORT}`);
});