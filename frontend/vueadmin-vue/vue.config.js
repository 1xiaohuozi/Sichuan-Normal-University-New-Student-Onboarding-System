const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      args[0].title = "四川师范大学迎新系统";//标题
      return args;
    });
  },
  transpileDependencies: true,
  devServer: {//关闭 webpack-dev-server 的错误提示遮罩层
    client: {
      overlay: false,
    },
  },
})
