<template>
  <div>
    <button @click="fetchVideo">获取视频</button>
    <p>{{ message }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: ""
    };
  },
  methods: {
    fetchVideo() {
      fetch("/api/video")  // 请求仍然发到 /api/video，代理会处理这个请求
        .then((response) => response.json())
        .then((data) => {
          this.message = data.message;  // 后端返回的 "This is a video file" 将会显示在页面上
        })
        .catch((error) => {
          this.message = "CORS 错误: " + error;
        });
    }
  }
};
</script>

<style>
/* 你可以根据需要自定义样式 */
</style>