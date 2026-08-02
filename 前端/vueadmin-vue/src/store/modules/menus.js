import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default {
	state: {
		// 存储菜单列表、权限列表和路由状态
		menuList: [],
		permList: [],
		hasRoutes: false,

		// 可编辑标签页的值和初始标签页
		editableTabsValue: 'Index',
		editableTabs: [
			{
				title: '首页',
				name: 'Index',
			}
		],
		tabIndex: 2 // 标签页索引
	},
	getters: {
		// 可以添加用于筛选、处理和派生 state 数据的 getters
	},
	mutations: {
		setMenuList(state, menus) {
			// 设置菜单列表
			state.menuList = menus;
		},
		setPermList(state, perms) {
			// 设置权限列表
			state.permList = perms;
		},
		changeRouteStatus(state, hasRoutes) {
			// 更改路由状态
			state.hasRoutes = hasRoutes;
		},
		addTab(state, tab) {
			// 添加新的标签页
			let index = state.editableTabs.findIndex(e => e.name === tab.name);
			if (index === -1) {
				state.editableTabs.push({
					title: tab.title,
					name: tab.name,
				});
			}
			state.editableTabsValue = tab.name;
		},
		resetState: (state) => {
			// 重置状态
			state.menuList = [];
			state.permList = [];
			state.hasRoutes = false;
			state.editableTabsValue = 'Index';
			state.editableTabs = [{
				title: '首页',
				name: 'Index',
			}];
		}
	},
	actions: {
		// 可以添加处理异步逻辑的 actions
	},
	modules: {
		// 可以添加其他模块，用于更好地组织状态管理
	}
}
