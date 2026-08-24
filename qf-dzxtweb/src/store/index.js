import Vue from "vue";
import Vuex from "vuex";
import app from "./modules/app";
import user from "./modules/user";
import tagsView from "./modules/tagsView";
import permission from "./modules/permission";
import settings from "./modules/settings";
import book from "./modules/book";
import getters from "./getters";

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    app,
    user,
    tagsView,
    permission,
    settings,
    book
  },
  getters,
  state: {
    subjectList: [], //最后一级科目
    assistAList: {}
  },
  mutations: {
    //最后一级科目
    getSubjectData(state, data) {
      state.subjectList = data;
    },

    //辅助核算
    getAccountingList(state, data) {
      state.assistAList = data;
    }
  }
});

export default store;
