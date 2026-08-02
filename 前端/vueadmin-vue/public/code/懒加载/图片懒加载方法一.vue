<template>
  <!-- 图片元素，使用 v-lazy 指令实现懒加载 -->
  <img v-lazy="imageSource" alt="懒加载图片" />
</template>

<script>
export default {
  data() {
    return {
      // 图片路径，此处应替换为实际的图片路径
      imageSource: "../assets/sicnu.png",
    };
  },
  directives: {
    // 自定义指令 v-lazy 实现图片懒加载
    "v-lazy": {
      // inserted 钩子在元素插入到 DOM 中时执行
      inserted(el) {
        // 创建 IntersectionObserver 实例，监测元素是否进入视窗
        const observer = new IntersectionObserver((entries) => {
          entries.forEach((entry) => {
            // 如果元素进入视窗
            if (entry.isIntersecting) {
              // 将元素的 src 设置为 data-src，加载图片
              el.src = el.dataset.src;
              // 停止观察该元素，以防止重复加载
              observer.unobserve(el);
            }
          });
        });

        // 开始观察当前元素
        observer.observe(el);
      },
    },
  },
};
</script>
