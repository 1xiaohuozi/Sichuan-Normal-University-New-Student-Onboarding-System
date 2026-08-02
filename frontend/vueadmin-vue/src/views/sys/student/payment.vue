<template>
  <div id="container1">
    <div v-if="statu === 1" style="text-align: center; margin-top: 80px">
      <img
        src="@/assets/view.png"
        alt=""
        style="width: 350px; height: 150px; margin: 0 auto; display: inline-block"
      />
    </div>

    <div v-else-if="statu === 2">
      <p style="color: green; font-weight: bold; font-size: 50px">
        <img
          src="@/assets/pass.png"
          alt=""
          style="vertical-align: middle; margin-right: 8px"
        />
        已通过缴费审核，缴费成功
      </p>
    </div>

    <div v-else>
      <div id="localpay">
        <div id="localpayment">
          <p id="p1">现场支付学费</p>
        </div>
        <div id="container">
          <div class="demo">
            <p id="p3">请将付费单据拍下来，并在下方提交给管理员审核</p>
            <div id="pic">
              <input
                type="file"
                class="input_flie"
                name="form_data"
                ref="file"
                id="chooseImg"
                @change="reads($event)"
              />
              <div class="div_shuline" v-if="imgUrl == ''"></div>
              <div class="div_hengline" v-if="imgUrl == ''"></div>
              <img
                v-if="imgUrl !== ''"
                :src="imgUrl"
                alt="Preview"
                class="preview-image"
              />
            </div>
          </div>
          <div class="demo">
            <p id="p3">请将身份证正面照拍下来，并在下方提交给管理员审核</p>
            <div id="pic2">
              <input
                type="file"
                class="input_flie"
                name="form_data"
                ref="file"
                id="chooseImg"
                @change="reads2($event)"
              />
              <div class="div_shuline" v-if="imgUrl2 == ''"></div>
              <div class="div_hengline" v-if="imgUrl2 == ''"></div>
              <img
                v-if="imgUrl2 !== ''"
                :src="imgUrl2"
                alt="Preview"
                class="preview-image"
              />
            </div>
          </div>
          <el-button id="btn" @click="confirmUpload" icon="el-icon-upload"
            >点击上传</el-button
          >
        </div>
        <br />
        <p v-if="statu === 3" style="color: red; font-weight: bold; text-align: center">
          <img
            src="@/assets/failed.png"
            alt=""
            style="width: 30px; height: 30px; vertical-align: middle; margin-right: 8px"
          />
          被打回，审核失败请重新提交
        </p>
      </div>
    </div>
  </div>
</template>
<style scoped>
.preview-image {
  max-width: 100%;
  max-height: 250px; /* 设置预览图片的最大高度 */
  margin-top: 10px;
}
#container1 {
  display: flex;
  justify-content: center;
}

#localpay {
  position: relative;
  width: 920px;
  /* height: 700px; */
  margin-top: -10px;
  margin-left: -10px;
}

#localpayment {
  width: 100%;
  min-height: 50px;
  border-width: 0.5px;
  background-color: rgba(39, 110, 81, 0.8);
  display: flex;
  justify-content: center;
}

#container {
  margin-top: 0;
  min-height: 360px;
  display: flex;
  justify-content: space-around;
}

.demo {
  display: inline-block;
  width: 50%;
  background-color: #f4f8f6;
  border-width: 1px;
}

.demo div {
  height: 200px;
  width: 250px;
  background-color: #797979;
  opacity: 0.3;
  margin-left: 100px;
  margin-top: 20px;
}

.demo div .input_flie {
  display: block;
  width: 250px;
  height: 200px;
  opacity: 0;
  z-index: 100;
  position: absolute;
  cursor: pointer;
}

.demo div .div_shuline {
  position: absolute;
  width: 150px;
  height: 1.5px;
  background-color: black;
  margin-top: 95px;
  z-index: 5;
  margin-left: 50px;
}

.demo div .div_hengline {
  position: absolute;
  width: 1px;
  height: 130px;
  background-color: black;
  margin-left: 120px;
  margin-top: 30px;
  z-index: 5;
}

.demo button {
  width: 200px;
  height: 40px;
  background-color: #409eff;
  color: white;
  margin-top: 30px;
  margin-left: 125px;
  outline: none;
  border: none;
  border-radius: 0.5em;
  cursor: pointer;
}

a {
  margin-left: 90%;
  /* margin-top: 30px; */
  cursor: pointer;
  color: blue;
  font-size: 13px;
}

#p1 {
  font-size: 18px;
  margin-top: 15px;
  /* font-weight: bolder; */
  color: #ffffff;
}

#p2 {
  margin-top: 25px;
  margin-left: 100px;
  font-size: 16px;
  font-weight: bold;
  color: #797979;
}

#p3 {
  margin-top: 25px;
  font-weight: bold;
  /* margin-left: 105px; */
  font-size: 14px;
  text-align: center;
}
</style>
<script>
import axios from "axios";
import { Message, MessageBox } from "element-ui";

export default {
  data() {
    return {
      imgUrl: "",
      imgUrl2: "",
      userInfo: {
        id: 1,
        username: "",
      },
      statu: 0,
    };
  },
  created() {
    this.getUserInfo().then(() => {
      this.fetchStatusFromBackend();
    });
  },
  methods: {
    getUserInfo() {
      return this.$axios.get("/sys/userInfo").then((res) => {
        this.userInfo = res.data.data;
        // console.log(this.userInfo.id);
      });
    },
    fetchStatusFromBackend() {
      // 确保在继续之前已经更新了this.userInfo
      console.log(this.userInfo.id);
      this.$axios
        .post("/sys/payment/info", this.userInfo.id)
        .then((response) => {
          this.statu = response.data.data.statu;
          // console.log(response.data.data.statu);
        })
        .catch((error) => {
          console.error("获取状态码时发生错误:", error);
        });
    },

    reads(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.imgUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    reads2(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.imgUrl2 = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    confirmUpload() {
      MessageBox.confirm("确认上传这两张图片吗？", "确认上传", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.upload();
        })
        .catch(() => {
          Message.info("已取消上传");
        });
    },
    upload() {
      const imageData1 = this.imgUrl;
      const imageData2 = this.imgUrl2;

      if (!imageData1 || !imageData2) {
        Message.error("请先选择文件");
        return;
      }

      const base64Data1 = imageData1.split(",")[1];
      const base64Data2 = imageData2.split(",")[1];

      axios
        .post("/sys/payment/save", {
          userId: this.userInfo.id,
          username: this.userInfo.username,
          name: this.userInfo.name,
          paymentImage: base64Data1,
          idCardImage: base64Data2,
          statu: "1", // 重置审核状态
        })
        .then((response) => {
          Message.success("上传成功");
          this.fetchStatusFromBackend();
        })
        .catch((error) => {
          Message.error("上传失败");
          console.error("上传图片时发生错误:", error);
        });
    },
  },
};
</script>
