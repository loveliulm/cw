<template>
  <div class="content">
    <p class="nav">工商执行单</p>
    <el-form :model="moneyxin" ref="moneyxinn" :inline="true" label-width="68px">
      <el-form-item label="客户名称" prop="total">
        <el-input
          v-model="moneyxin.customerName"
          placeholder="请输入客户名称"
          clearable
          disabled
          size="small"
          :readonly="true"
          style="width: 295px"
        ></el-input>
      </el-form-item>
      <el-form-item label="服务内容" prop="amount">
        <el-input
          v-model="moneyxin.serviceItemName"
          placeholder="请输入服务内容"
          clearable
          size="small"
          disabled
          :readonly="true"
          type="text"
          style="width: 295px"
        ></el-input>
      </el-form-item>
      <el-form-item label="联系人" prop="representative">
        <el-input
          v-model="moneyxin.representative"

          clearable
          disabled
          size="small"
          :readonly="true"
          style="width: 295px"
        ></el-input>
      </el-form-item>
      <el-form-item label="联系电话" prop="normalContactPhone">
        <el-input
          v-model="moneyxin.normalContactPhone"
          placeholder="请输入联系电话"
          clearable
          disabled
          size="small"
          :readonly="true"
          style="width: 295px"
        ></el-input>
      </el-form-item>
      <el-form-item label="截至时间" prop="contractEndDate">
        <el-date-picker
          clearable
          size="small"
          style="width: 295px"
          v-model="moneyxin.contractEndDate"
          type="date"
          disabled
          :readonly="true"
          value-format="yyyy-MM-dd"
          placeholder="选择结束收款日期"
        ></el-date-picker>
      </el-form-item>
      <!-- <el-form-item label="任务说明" prop="remark">
        <el-input
          v-model="moneyxin.remark"
          placeholder="请输入任务说明"
          clearable
          size="small"
          :readonly="true"
          style="width: 295px"
        ></el-input>
      </el-form-item>-->
    </el-form>
    <div class="dotted"></div>
    <div class="steps">
      <div
        class="steps-inside"
        v-for="(item,ind) in gongList"
        :key="ind"
        :class="{'steps-insides':extent==ind ,'steps-insidess':item.stepStatus == 1,'steps-insidesss':item.stepStatus == 0}"
      >
        <div v-show="item.Statuss == 1" class="circle" @click="acquire(ind)">
          {{item.stepOrder}}
          <div></div>
        </div>
        <div
          v-show="item.stepStatus == 1"
          class="el-icon-circle-check tick"
          style="font-size:27px;color:#008cff"
        >
          <div></div>
        </div>

        <div v-show="item.stepStatus == 0" @click="acquire(ind)" class="circle-x">
          {{item.stepOrder}}
          <div></div>
        </div>
        <div class="inside-name" :title="item.stepName">{{item.stepName}}</div>
        <div class="inside-course">
          <span v-show="item.stepStatus == 0">进行中</span>
          <span v-show="item.stepStatus == 1">已完成</span>
        </div>
        <div
          :class="[currentIdx == ind?'inside-contents':'']"
          class="inside-content"
          @mouseleave="leave()"
          @mouseenter="enter(ind)"
          v-if="item.Status == 1 ? true : false"
        >
          <p class="inside-content-p">步骤{{item.stepOrder}}：{{item.stepName}}</p>
          <div class="inside-content-div">
            <p class="inside-content-div-p">
              描述:
              <span v-show="item.stepDescribe === null">无</span>
              <span v-show="item.stepDescribe !== null">{{item.stepDescribe}}</span>
            </p>
            <p class="inside-content-div-p">
              资料描述:
              <span v-show="item.stepData === null">无</span>
              <span v-show="item.stepData !== null">{{item.stepData}}</span>
            </p>
            <p class="inside-content-div-p">
              交付件:
              <span v-show="item.stepOutput === null">无</span>
              <span v-show="item.stepOutput !== null">{{item.stepOutput}}</span>
            </p>
             <el-upload
               class="upload-demo"
               action=""
               ref="upload"
               :before-upload="beforeUpload"
               :http-request="uploadFile"
               :on-preview="previewFile"
               multiple
               :limit="3"
               :on-exceed="handleExceed"
               :on-error="onErrorFileUpload"
               :before-remove="handleRemove"
               :file-list="fileList"
            >
              <span style="color: #323232;font-size: 14px;margin-right:20px">附件:</span>
              <el-button icon="el-icon-upload2" size="small" type="primary" @click="handleClick(item.id)">附件上传</el-button>
            </el-upload>
<!--             <div class="demo-input-suffix">-->
<!--              <span>完成说明：</span>-->
<!--              <el-input placeholder="请输入完成说明" style="width: 650px" v-model="remark"></el-input>-->
<!--            </div>-->
            <br />
            <el-form
              :model="alterList"
              :rules="rules"
              ref="alterList"
              :inline="true"
              label-width="85px"
            >
              <el-form-item label="完成说明" prop="remark">
                <el-input placeholder="请输入完成说明" type="textarea" :row="3" style="width: 650px" v-model="alterList.remark"></el-input>
              </el-form-item>
              <br />
              <el-form-item label="完成人" prop="operatorUserId">
                <el-select
                  v-model="alterList.operatorUserId"
                  placeholder="请输入完成人"
                  clearable
                  size="small"
                  style="width: 270px"
                >
                  <el-option
                    v-for="dict in accountManagers"
                    :key="dict.userId"
                    :label="dict.userName"
                    :value="parseInt(dict.userId)"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="终止日期" prop="completeTime">
                <el-date-picker
                  clearable
                  size="small"
                  style="width: 270px"
                  v-model="alterList.completeTime"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="选择结束收款日期"
                ></el-date-picker>
              </el-form-item>
            </el-form>

            <el-button v-hasPermi="['commerce:step:edit']" class="notarize" @click="alter(item.id)">完成</el-button>
          </div>
        </div>
        <div
          :class="[currentIdx == ind?'inside-contents':'']"
          @mouseleave="leave()"
          @mouseenter="enter(ind)"
          class="inside-content"
          v-show="item.stepStatus == 1 ? true : false"
        >
          <p class="inside-content-p">步骤{{item.stepOrder}}：{{item.stepName}}</p>
          <div class="inside-content-div">
            <p class="inside-content-div-p">
              完成时间:
              <span v-show="item.completeTime === null">无</span>
              <span
                v-show="item.completeTime !== null"
              >{{parseTime (item.completeTime, '{y}-{m}-{d}') }}</span>
            </p>
            <p class="inside-content-div-p">
              完成说明:
              <span v-show="item.remark === null">无</span>
              <span v-show="item.remark !== null">{{item.remark}}</span>
            </p>
            <p class="inside-content-div-p">
              交付件:
              <span v-show="item.stepOutput === null">无</span>
              <span v-show="item.stepOutput !== null">{{item.stepOutput}}</span>
            </p>
            <p class="inside-content-div-p">
              附件:
              <span v-show="item.commerceStepFileList === null">无</span>
              <span v-for="(item, index) in item.commerceStepFileList" :key="index" style="color: #20a0ff; cursor: pointer; margin: 0 10px;" @click="previewFile(item)">
                {{ item.fileName }}
              </span>
            </p>
            <p class="inside-content-div-p">
             完成人:
              <!-- <span v-show="item.operatorUserId === null">无</span> -->
              <span>{{item.operatorUserName}}</span>
            </p>
            <el-button v-hasPermi="['commerce:step:revocation']"   class="notarizes" @click="revocation(item.id)">撤销完成</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { getToken } from "../../../utils/auth";
import {
  listbusiness,
  alterbusiness,
  deletebusiness,
  uploadStepFile,
  previewStepFile,
  getStepFile,
  deleteStepFile,
} from "@/api/commerce/businessServes";
import { getPersonList } from "@/api/customer/customer";
export default {
  name: "businessServes",
  data() {
    return {
      moneyxin: {}, //工商执行信息
      gongList: [], //工商步骤执行信息
      rules: {
        operatorUserId: [
          { required: true, message: "完成人不能为空", trigger: "blur" },
        ],
        completeTime: [
          { required: true, message: "终止日期不能为空", trigger: "blur" },
        ],
      },
      // 工商外勤
      accountManagers: [],
      //步骤完成数据
      alterList: {
        //  id: undefined, //提交id
        //  operatorUserId: undefined, //完成人
        //  completeTime: undefined, //完成时间
        // remark: undefined, //完成备注
      },
      //列表长度
      extent: "",
      //鼠标移入变色
      currentIdx: null,
      fileList: []

    };
  },
  mounted() {},
  created() {
    this.getList();
    this.getAccountManager();
  },
  methods: {
    //列表获取
    getList() {
      const id = this.$route.query.tableId;
      listbusiness(id).then((response) => {
        this.moneyxin = response.commerceService;
        this.gongList = response.data;

        this.extent = response.data.length - 1;
        for (let i = 0; i < this.gongList.length; i++) {
          this.gongList[i].Status = 0;
          this.gongList[i].Statuss = 0;
        }
        for (let i = 0; i < this.gongList.length; i++) {
          if (this.gongList[i].stepStatus == 0) {
            this.gongList[i].Status = 1;
            this.gongList[i].Statuss = 1;
            this.getFileBaseInfo(this.gongList[i].id)
            break;
          }
        }
        // console.log(this.gongList);
      });
    },
    // 获取工商外勤列表
    getAccountManager: function () {
      getPersonList({ roleKey: "legwork" }).then((response) => {
        this.accountManagers = response.data;
        //this.alterList.operatorUserId =this.moneyxin.operatorUserId
        this.$set(this.alterList,'operatorUserId',this.moneyxin.operatorUserId)
      });
    },
    // 步骤提交
    alter(row) {
      this.alterList.id = row;
      console.log(this.$refs["alterList"])
      this.$refs["alterList"][0].validate((valid) => {
        if (valid) {
          alterbusiness(this.alterList).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("提交成功");
              this.getList();
              this.reset();
              this.alterList.operatorUserId =this.moneyxin.operatorUserId
            }
          });
        } else {
          // this.$message({
          //   showClose: true,
          //   message: "修改失败，不能为空",
          //   type: "error",
          // });
        }
      });
    },
    //步骤撤销
    revocation(id) {
      this.$confirm("你确定要撤销该完成的步骤吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deletebusiness(id).then((response) => {
          if (response.code === 200) {
            this.msgSuccess("撤销成功");
            this.getList();
            this.reset();
            this.alterList.operatorUserId =this.moneyxin.operatorUserId
          }
        });
      });
    },
    // 表单重置
    reset() {
      this.alterList = {
        id: undefined, //提交id
        operatorUserId: undefined, //完成人
        completeTime: undefined, //完成时间
        remark: undefined, //完成备注
      };
      this.resetForm("form");
    },
    //点击显示隐藏
    acquire(ind) {

      var array = this.gongList[ind];
      if (this.gongList[ind].Status == 0) {
        this.$forceUpdate();
        for (let i = 0; i < this.gongList.length; i++) {
          this.gongList[i].Statuss = 0;
          this.gongList[i].Status = 0;
        }
        this.$set(this.gongList[ind], "Status", 1);
        this.$set(this.gongList[ind], "Statuss", 1);
         this.alterList.operatorUserId =this.moneyxin.operatorUserId
         this.alterList.completeTime =undefined

      } else {
        this.$forceUpdate();
        for (let i = 0; i < this.gongList.length; i++) {
          this.gongList[i].Statuss = 0;
          this.gongList[i].Status = 0;
        }

        this.$set(this.gongList[ind], "Status", 0);
        this.$set(this.gongList[ind], "Statuss", 0);
      }
    },
    // 鼠标移入变色
    enter(idx) {
      this.currentIdx = idx;
      // console.log(idx);
    },
    //鼠标移出
    leave() {
      this.currentIdx = null;
    },
    handleClick(id) {
      this.alterList.id = id
    },
    uploadFile({ file }) {
      let formData = new FormData();
      formData.append("stepId", this.alterList.id);
      formData.append("files", file);
      uploadStepFile(formData).then(res=> {
        if (res.code == 200) {
          this.getFileBaseInfo(this.alterList.id)
          this.$message.success("上传成功")
        }
      })
    },
    previewFile(file){
      previewStepFile({'fileUrl': file.fileUrl}).then(res => {
        console.log(res)
        const blob = new Blob([res], { type: res.type })
        const url = URL.createObjectURL(blob)
        window.open(url)
      })
    },
    beforeUpload(file){
      //上传前校验
      if(file.size > 30000000){
        this.$message.error("文件上传失败, 文件大小超出限制")
        return false
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    onErrorFileUpload(err, file, fileList){
      this.$message.error("文件上传失败")
    },
    handleRemove(file, fileList) {
      return new Promise((response, reject) => {
        this.$confirm('此操作将删除该附件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteStepFile(file.id).then(res => {})
          return response();
        }).catch(() => {
          return reject();
        });
      })
    },
    getFileBaseInfo(stepId) {
      getStepFile(stepId).then(res => {
        res.data.map(item => {
          item.name = item.fileName
          item.url = item.fileUrl
        })
        this.fileList = res.data
      })
    },
  },
};
</script>

<style lang="scss" scoped>
.content {
  padding: 20px;
  //border: 1px solid red;
  min-height: calc(100vh - 84px);
  .nav {
    width: 100%;
    text-align: center;
    margin: 0 0 20px 0;
    font-size: 16px;
    font-weight: 700;
  }
  .dotted {
    width: 100%;
    height: 10px;
    border-bottom: 1px solid rgb(226, 226, 226);
  }
  .steps {
    overflow: auto;
    padding: 16px 16px 16px 35px;
    overflow: auto;
    overflow-y: visible;
    // border: 1px solid #bfbfbf;
    height: 630px;
    cursor: pointer;
    .steps-insides {
      border: 0 !important;
    }
    .steps-insidess {
      border-left: 1px solid #008cff;
    }
    .steps-insidesss {
      border-left: 1px solid rgb(226, 226, 226);
    }
    .steps-inside {
      width: 100%;
      padding: 10px 20px 10px 20px;
      min-height: 50px;
      //border-left: 1px solid rgb(226, 226, 226);
      position: relative;
      margin-top: 5px;
      .circle {
        position: absolute;
        left: -15.7px;
        top: -3px;
        width: 30px;
        height: 30px;
        margin-right: 8px;
        font-size: 17px;
        line-height: 30px;
        text-align: center;
        background: #008cff;
        border-radius: 30px;
        color: white;
        -webkit-transition: background-color 0.3s, border-color 0.3s;
        transition: background-color 0.3s, border-color 0.3s;
        z-index: 100;

        div {
          width: 30px;
          height: 5px;
          background: white;
        }
      }
      .tick {
        position: absolute;
        left: -13.5px;
        top: -1px;
        background: white;
        //display: none;
        -webkit-transition: background-color 0.3s, border-color 0.3s;
        transition: background-color 0.3s, border-color 0.3s;
        div {
          width: 30px;
          height: 5px;
          background: white;
        }
      }
      .circle-x {
        position: absolute;
        left: -12.5px;
        top: -1px;
        width: 25px;
        height: 25px;
        margin-right: 8px;
        font-size: 17px;
        line-height: 22.5px;
        text-align: center;
        background: white;
        border-radius: 25px;
        border: 2px solid #008cff;
        color: #008cff;
        -webkit-transition: background-color 0.3s, border-color 0.3s;
        transition: background-color 0.3s, border-color 0.3s;

        div {
          width: 30px;
          height: 5px;
          background: white;
          position: relative;
          top: 1px;
        }
      }
      .inside-name {
        width: 100%;
        position: absolute;
        font-size: 14px;
        top: 6px;
        width: 100px;
        //background: yellow;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        letter-spacing: 1.5px;
      }
      .inside-course {
        font-size: 13px;
        position: absolute;
        top: 35px;
        color: #bfbfbf;
      }
      .inside-contents {
        border: 1px solid #008cff !important;
      }
      .inside-content {
        width: 90%;
        height: 100%;
        //background: wheat;
        position: relative;
        border: 1px solid #bfbfbf;
        left: 130px;
        top: -3px;
        .inside-content-p {
          font-size: 16px;
          line-height: 16px;
          padding: 14px 0 12px;
          color: #000;
          font-weight: 700;
          text-indent: 20px;
        }
        .inside-content-div {
          width: 90%;
          height: 100%;
          //background: gold;
          margin: 0 auto;
          .inside-content-div-p {
            color: #323232;
            font-size: 14px;
            line-height: 1.4;
          }
        }
      }
      .demo-input-suffix {
        margin: 20px 0;
        span {
          font-size: 14px;
          color: #323232;
          line-height: 1;
        }
      }
      .notarize {
        padding: 9px 15px;
        font-size: 13px;
        border-radius: 3px;
        background: #1890ff;
        color: white;
        width: 70px;
        text-align: center;
        margin-left: 95%;
        margin-bottom: 17px;
        margin-top: -17px;
        letter-spacing: 3px;
      }
      .notarizes {
        padding: 9px 15px;
        font-size: 13px;
        border-radius: 3px;
        //background: #ff4949;
        border: 1px solid #dcdfe6;
        color: #606266;
        width: 92px;
        text-align: center;
        margin-left: 95%;
        margin-bottom: 17px;
        margin-top: -17px;
        letter-spacing: 2px;
      }
    }
  }
}
/deep/.el-upload-list__item {
  width: 30%;
}
/deep/.el-form-item__label {
  text-align: left;
}
</style>
