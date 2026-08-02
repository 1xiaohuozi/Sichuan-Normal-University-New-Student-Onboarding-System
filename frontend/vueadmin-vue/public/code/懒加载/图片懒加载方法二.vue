// <template>
//   <div>
//     <img v-lazy src="../assets/logo2.png" alt="Your Image" class="login-image" />
//   </div>
// </template>

<div v-lazy-container="{ selector: 'img' }">
  <img data-src="//aaa.com/img1.jpg">
  <img data-src="//aaa.com/img2.jpg">
  <img data-src="//aaa.com/img3.jpg">  
</div>
  
<!--或者这种：-->
 
 <div v-lazy-container="{ selector: 'img' }" v-html="content">
</div>

