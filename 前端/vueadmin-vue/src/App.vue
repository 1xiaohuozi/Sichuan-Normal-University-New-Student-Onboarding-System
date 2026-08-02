<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
	
	export default{
		name:"App",
		watch:{
			$route(to,from){
				if(to.path != '/login'){
					let obj = {
						name: to.name,
						title: to.meta.title
					}
					this.$store.commit("addTab",obj)
				}
			}
		}
	}
	
</script>

<style>
	
body html  {
  margin: 0;
  padding: 0;
  height: 100%;
}
#app {
  margin: 0;
  padding: 0;
  height: 800px;
}
.router-view{
	height: 100%;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
