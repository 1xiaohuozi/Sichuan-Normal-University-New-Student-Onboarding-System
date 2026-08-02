import Vue from "vue";
import VueRouter from "vue-router";
import axios from "axios";
import store from "../store";

Vue.use(VueRouter);


/*
懒加载处理路由组件
*/
const routes = [
	{
		path: "/",
		name: "Home",
		component: () => import("../views/Home.vue"),
		children: [
			{
				path: "/index",
				name: "Index",
				meta: {
					title: "首页",
				},
				component: () => import("@/views/index.vue"),
			},
			{
				path: "/userCenter",
				name: "UserCenter",
				meta: {
					title: "修改密码",
				},
				component: () => import("@/views/UserCenter.vue"),
			},
			{
				path: "/sys/users",
				name: "SysUser",
				component: () => import("../views/sys/User.vue"),
			},
			{
				path: "/sys/roles",
				name: "SysRole",
				component: () => import("../views/sys/Role.vue"),
			},
			{
				path: "/sys/menus",
				name: "SysMenu",
				component: () => import("../views/sys/Menu.vue"),
			},
			{
				path: "/sys/report",
				name: "SysReport",
				component: () => import("../views/sys/Report.vue"),
			},
			{
				path: "/sys/student/payment",
				name: "Payment",
				component: () => import("../views/sys/student/payment.vue"),
			},
			{
				path: "/sys/student/reported",
				name: "Reported",
				component: () => import("../views/sys/student/reported.vue"),
			},
			{
				path: "/sys/student/live",
				name: "Live",
				component: () => import("../views/sys/student/live.vue"),
			},
			{
				path: "/sys/reportlist",
				name: "Reportlist",
				component: () => import("../views/sys/Reportlist.vue"),
			},
			{
				path: "/sys/paymanage",
				name: "Paymanage",
				component: () => import("@/views/sys/paymanage.vue"),
			},
			{
				path: "/sys/student/messageconfirm",
				name: "Messageconfirm",
				component: () => import("@/views/sys/student/messagecomfirm.vue"),
			},
			{
				path: "/sys/student/greenway",
				name: "Greenway",
				component: () => import("@/views/sys/student/greenway.vue"),
			},
			{
				path: "/sys/greenwaymanage",
				name: "Greenwaymanage",
				component: () => import("@/views/sys/greenwaymanage.vue"),
			},
			{
				path: "/sys/logmanage",
				name: "Logmanage",
				component: () => import("@/views/sys/logmanage.vue"),
			},
			{
				path: "/sys/studentmanage",
				name: "Studentmanage",
				component: () => import("@/views/sys/studentmanage.vue"),
			},
			{
				path: "/sys/imageviewall",
				name: "imageviewall",
				component: () => import("@/views/sys/imageviewall.vue"),
			},
			{
				path: "/sys/studentview",
				name: "Studentview",
				component: () => import("@/views/sys/studentview.vue"),
			},
			{
				path: "/sys/student/dormitory",
				name: "Dormitory",
				component: () => import("@/views/sys/student/Dormitory.vue"),
			},
			{
				path: "/sys/dormitorymanage",
				name: "Dormitorymanage",
				component: () => import("@/views/sys/dormitorymanage.vue"),
			},
		],
	},
	{
		path: "/main",
		name: "Main",
		meta: {
			title: "首页",
		},
		component: () => import("@/views/main/main.vue"),
	},
	{
		path: "/login",
		name: "Login",
		component: () => import("@/views/Login.vue"),
	},
	{
		name: "notfound",
		path: "/notfound",
		component: () => import("@/views/404/NotFound.vue"),
	},

	{
		path: "/announcement",
		name: "announcementDetail",
		component: () => import('@/views/main/AnnouncementDetails.vue'),
	},

	/*
	  处理404
	  */
	{
		path: "/:pathMatch(.*)*",
		redirect: "/notfound",
	},
];

const router = new VueRouter({
	mode: "history",
	base: process.env.BASE_URL,
	routes,
});

router.beforeEach((to, from, next) => {
	let hasRoute = store.state.menus.hasRoutes;
	let token = localStorage.getItem("token");

	if (to.path === "/login") {
		next();
	} else if (to.path === "/main" || to.path === "/announcement") {
		// 如果访问的是 /main 页面，无论登录状态如何都允许访问
		next();
	} else if (!token) {
		next({ path: "/main" });
	} else if (token && !hasRoute) {
		axios
			.get("/sys/menu/nav", {
				headers: {
					Authorization: localStorage.getItem("token"),
				},
			})
			.then((res) => {
				// console.log("Menu Data:", res.data.data.nav);

				// 拿到menuList
				store.commit("setMenuList", res.data.data.nav);

				// 拿到用户权限
				store.commit("setPermList", res.data.data.authoritys);

				// console.log(store.state.menus.menuList);

				// 动态绑定路由
				let newRoutes = router.options.routes;

				res.data.data.nav.forEach((menu) => {
					if (menu.children) {
						menu.children.forEach((e) => {
							// 转成路由
							let route = menuToRoute(e);

							// 吧路由添加到路由管理中
							if (route) {
								newRoutes[0].children.push(route);
							}
						});
					}
				});

				// console.log("newRoutes");
				// console.log(newRoutes);
				router.addRoutes(newRoutes);

				hasRoute = true;
				store.commit("changeRouteStatus", hasRoute);

				// 在路由信息加载完成后再进行导航
				next({ ...to, replace: true });
			})
			.catch((error) => {
				console.error("获取菜单数据时出错:", error);
				next({ path: "/login" }); // 失败时跳转到登录页
			});
	} else {
		next();
	}
});

// 导航转成路由
const menuToRoute = (menu) => {
	if (!menu.component) {
		return null;
	}

	let route = {
		name: menu.name,
		path: menu.path,
		meta: {
			icon: menu.icon,
			title: menu.title,
		},
	};
	route.component = () => import("@/views/" + menu.component + ".vue");

	return route;
};

export default router;
