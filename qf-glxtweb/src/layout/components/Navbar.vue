<template>
  <div class="navbar">
    <hamburger id="hamburger-container"
               :is-active="sidebar.opened"
               class="hamburger-container"
               @toggleClick="toggleSideBar" />

    <!--<breadcrumb id="breadcrumb-container"
                class="breadcrumb-container" />-->
    <div class="breadcrumb-container"><span>{{user.companyName}}</span></div>
    <el-dropdown class=" right-menu-item hover-effect"
                   trigger="hover" style="float: left;">
        <div class="breadcrumb-containerss">
<!--          <img v-show="deptStatus == 0" src="../../../src/assets/logo/wei.svg" alt="">-->
<!--          <img v-show="deptStatus == 1" src="../../../src/assets/logo/ji.svg" alt="">-->
<!--          <img v-show="deptStatus == 2" src="../../../src/assets/logo/yi.svg" alt="">-->
          <el-link style="line-height: 50px; margin-left: 20px; font-size: 16px" v-show="deptStatus == 0" :underline="false" type="primary">账户信息</el-link>
          <el-link style="line-height: 50px; margin-left: 20px; font-size: 16px" v-show="deptStatus == 1" :underline="false" type="warning">账户信息</el-link>
          <el-link style="line-height: 50px; margin-left: 20px; font-size: 16px" v-show="deptStatus == 2" :underline="false" type="danger">账户信息</el-link>
        </div>
        <el-dropdown-menu slot="dropdown">
          <p v-show="deptStatus == 0" style="font-size: 15px;margin: 8px 10px;letter-spacing:1px;">当前状态: 未过期</p>
          <p v-show="deptStatus == 1" style="font-size: 15px;margin: 8px 10px;letter-spacing:1px;">当前状态: 即将过期</p>
          <p v-show="deptStatus == 2" style="font-size: 15px;margin: 8px 10px;letter-spacing:1px;">当前状态: 已过期</p>
          <p style="font-size: 15px;margin: 8px 10px;letter-spacing:1px;">开通账套数: {{zhang.bookNumber}}</p>
          <!-- <p>已使用账套数: {{zhang.occupied}}</p> -->
          <p style=" font-size: 15px;margin: 8px 10px;letter-spacing:1px;">到期时间: {{zhang.endTime}}</p>
          <p style=" font-size: 15px;margin: 8px 10px;letter-spacing:1px;">在用账套: {{zhang.occupied}}，删除账套: {{zhang.recycled}}</p>
        </el-dropdown-menu>
      </el-dropdown>
      <div v-show="deptStatus == 1" class="breadcrumb-containers">&nbsp;<i class="el-icon-message">&nbsp;</i>亲爱的用户您好，您的诚信财税管家系统即将到期，如需继续使用，请重新续费，续费购买请咨询热线：xxxxx</div>
    <div v-show="deptStatus == 2" class="breadcrumb-containers">&nbsp;<i class="el-icon-message">&nbsp;</i>亲爱的用户您好，您的诚信财税管家系统已过期，现仅支持数据查询，如需继续使用，请重新购买开通，购买开通请咨询热线：xxxxx</div>
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
import { mapState } from 'vuex'
import { getInfo,zhangList } from "@/api/login";
export default {
  data(){
    return{
      deptStatus:0,
      zhang:[]
    }
  },
  components: {
    //Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapState([
      // 映射 this.name 为 store.state.name
      'user',
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
    getList(){
      getInfo().then(response => {
       this.deptStatus =response.user.dept.deptStatus

      });
       zhangList().then(response => {
       this.zhang=response.data

      });
    },
    toggleSideBar () {
      this.$store.dispatch('app/toggleSideBar')
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
    }
  },
   created () {
    this.getList();
   }

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
  .breadcrumb-containers {
      float: left;
      font-size: 14px;
      letter-spacing: 1px;
      line-height: 20px;
      background: #fff8ee;
      color: #ff6400;
      margin-top: 13.5px;
      margin-left: 20px;
  }
  .zhangtao p{
    font-size: 14px;
    margin: 0 10px;

  }

  .breadcrumb-containerss img{
    width: 30px;
    margin-top: 10px;
    margin-left: 15px;
    margin-right: 15px;

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

  .el-dropdown {
    padding-right: 20px;
  }
}
</style>
