<template>
  <div class="navbar">
    <hamburger id="hamburger-container"
               :is-active="sidebar.opened"
               class="hamburger-container"
               @toggleClick="toggleSideBar" />

    <!--<breadcrumb id="breadcrumb-container"
                class="breadcrumb-container" />-->
    <div class="breadcrumb-container">
<!--      <span>{{book.customerName == ''?customerName:book.customerName}}</span>-->
      <el-select
        v-model="bookId"
        value-key="bookId"
        filterable
        placeholder="请选择账套"
        @change="changeBook"
      >
        <el-option
          v-for="item in bookList"
          :key="item.bookId"
          :label="item.customerName"
          :value="item.bookId + ''"
        ></el-option>
      </el-select>
      <span style="font-size:16px;">{{book.currentPeriodData.str_before?book.currentPeriodData.str_before:currentPeriodData.str_before}}年{{book.currentPeriodData.str_after?book.currentPeriodData.str_after:currentPeriodData.str_after}}期</span>
    </div>
    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search"
                class="right-menu-item"
                v-if="false" />

        <el-tooltip content="源码地址"
                    effect="dark"
                    placement="bottom"
                    v-if="false">
          <ruo-yi-git id="ruoyi-git"
                      class="right-menu-item hover-effect"
                      v-if="false" />
        </el-tooltip>

        <el-tooltip content="文档地址"
                    effect="dark"
                    placement="bottom"
                    v-if="false">
          <ruo-yi-doc id="ruoyi-doc"
                      class="right-menu-item hover-effect" />
        </el-tooltip>

        <screenfull id="screenfull"
                    class="right-menu-item hover-effect"
                    v-if="false" />

        <el-tooltip content="布局大小"
                    effect="dark"
                    placement="bottom"
                    v-if="false">
          <size-select id="size-select"
                       class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect"
                   trigger="click">
        <div class="avatar-wrapper">
          <!--<img src="../../assets/icons/svg/user.svg"
               class="user-avatar">-->
          <i class="el-icon-s-custom user-avatar" />
          <span class="username">{{user.name}}</span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true"
                            v-if="false">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided
                            @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
//import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import { BookInfo, listBook } from "@/api/login"

import { mapState } from 'vuex'

export default {
  components: {
    //Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  data () {
    return {
      bookId: '',
      // 代账服务表格数据
      bookList: [],
      customerName: this.localStorage.getItem('customerName'),
      currentPeriodData: this.localStorage.getItem('currentPeriodData')
    }
  },
  created(){
    this.getList();
    this.getBookInfo();
  },
  computed: {
    ...mapState([
      // 映射 this.name 为 store.state.name
      'user',
      'book'
    ]),
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get () {
        return this.$store.state.settings.showSettings
      },
      set (val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    }
  },
  methods: {
    toggleSideBar () {
      this.$store.dispatch('app/toggleSideBar')
    },

    changeBook(val) {
      this.getBookInfo(val)
    },

    // 查询代账服务列表
    getList() {
      listBook({}).then((response) => {
        this.bookList = response.rows;
      });
    },
     // 获取当前账套信息
    getBookInfo (bookId) {
      if (bookId) {
        let query = window.location.href;
        let jhIndex = this.findStringIndex(query, '#', 0) ;
        let jhStr = query.substring(0,jhIndex);
        let xhlastIndex = jhStr.lastIndexOf('/') + 1;

        window.location.href = jhStr.substring(0, xhlastIndex) + bookId + '#/index'
      } else {
        let bookId = this.getUrlBookId();//this.$store.state.bookId == null ? this.localStorage.getItem("bookId") : this.$store.state.bookId
        this.bookId = bookId;
        BookInfo({ bookId }).then(res => {
          let data = res.data;
          this.customerName = res.data.customerName;
          this.currentPeriodData = this.getStr(res.data.currentPeriod, '-');
        })
      }
    },
    // 分割字符串
    getStr (string, str) {
      let str_before = string.split(str)[0];
      let str_after = string.split(str)[1];
      return { str_before, str_after };
    },
    async logout () {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.reload()
        })
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
    span {
      font-size: 18px;
      font-weight: 700;
      line-height: 50px;
    }
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 16px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        display: flex;
        justify-content: space-around;
        align-items: center;

        .user-avatar,
        .userName {
          cursor: pointer;
          /*width: 40px;
          height: 40px;*/
          border-radius: 10px;
          margin-right: 4px;
        }

        /*.el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }*/
      }
    }
  }
}
</style>
