import Vue from 'vue'
import Vuex from 'vuex'
import menus from "./modules/menus.js"

Vue.use(Vuex)

export default new Vuex.Store({
	state: {
		token: '' // 应用全局的 token 状态，用于存储用户认证令牌
	},
	getters: {
		// 这里可以添加用于筛选、处理和派生 state 数据的 getters
	},
	mutations: {
		SET_TOKEN: (state, token) => {
			// 设置 token 并存储到浏览器的 localStorage
			state.token = token
			localStorage.setItem("token", token)
		}
		// 这里可以添加其他同步修改 state 的 mutations
	},
	actions: {
		// 这里可以添加处理异步逻辑的 actions
	},
	modules: {
		menus // 将名为 'menus' 的模块整合到根 store 中
		// 可以添加其他模块，用于更好地组织状态管理
	}
})
