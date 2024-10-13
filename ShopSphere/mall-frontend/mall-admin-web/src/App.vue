<template>
  <div id="app">
    <h1>WebSocket VA Engine Status</h1>
    <div>
      <p :style="{ color: statusColor }">Status: {{ status }}</p>
      <ul>
        <li v-for="(message, index) in messages" :key="index">{{ message }}</li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      socket: null,
      status: "Waiting for update...",
      messages: [],
      statusColor: "black" // 默认颜色
    };
  },
  methods: {
    connectWebSocket() {
      // 替换为你的后端 WebSocket URL
      const vaEngineId = "1234"; // 示例的 vaEngineId
      this.socket = new WebSocket(`ws://localhost:8080/ws/AddVaEngine/${vaEngineId}`);

      this.socket.onmessage = (event) => {
        console.log("Message received: ", event.data);  // 日志输出收到的消息内容

        const data = JSON.parse(event.data);

        console.log("Parsed data: ", data);

        
        this.status = data.modelInstallStatus || "No status";
        this.messages = data.messages || [];

        // 根据 modelInstallStatus 改变颜色
        if (data.modelInstallStatus === "installing") {
          this.statusColor = "red";  // installing 显示为红色
        } else if (data.modelInstallStatus === "installed") {
          this.statusColor = "green";  // installed 显示为绿色
        } else {
          this.statusColor = "blue";  // 默认其他状态为蓝色
        }
      };

      this.socket.onopen = () => {
        console.log("WebSocket connection opened");
      };

      this.socket.onerror = (error) => {
        console.error("WebSocket error:", error);
      };

      this.socket.onclose = () => {
        console.log("WebSocket connection closed");
      };
    }
  },
  mounted() {
    // 组件挂载时启动 WebSocket 连接
    this.connectWebSocket();
  }
};
</script>

<style scoped>
h1 {
  color: #42b983;
}
p {
  font-size: 1.2em;
}
</style>
