const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8081,   // 前端运行在 8081 端口
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 代理到后端运行的地址
        changeOrigin: true,
        pathRewrite: { '^/api': '/api' } // 将 /api 去掉，直接转发请求
      }
    }
  }
})
