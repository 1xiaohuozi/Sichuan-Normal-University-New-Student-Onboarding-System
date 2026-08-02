import VueLazyload from "vue-lazyload";

Vue.use(VueLazyload);

//或者自定义配置插件
Vue.use(VueLazyload, {
    preLoad: 1.3,
    error: require('../src/assets/image/error.png'),
    loading: require('../src/assets/image/loading.gif'),
    attempt: 1
})


//这里存在一个坑，当图片放在 assets文件下 的时候，要使用上面的这种 require(‘相对路径’）的写法来进行引入。
//当图片放在 static文件下 的时候就可以直接写路径来进行引入，像下面的写法一样。
Vue.use(VueLazyload, {
    preLoad: 1.3,
    error: '../src/assets/image/error.png',
    loading: '../src/assets/image/loading.gif',
    attempt: 1
})
