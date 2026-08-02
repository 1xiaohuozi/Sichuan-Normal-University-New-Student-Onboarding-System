<template>
  <div id="container1">
    <div v-if="statu === 1" class="status-container">
      <img
        src="@/assets/view.png"
        alt=""
        style="width: 350px; height: 150px; margin: 0 auto; display: inline-block"
      />
    </div>

    <div v-else-if="statu === 2" class="status-container">
      <p style="color: green; font-weight: bold; font-size: 50px">
        <img
          src="@/assets/pass.png"
          alt=""
          style="vertical-align: middle; margin-right: 8px"
        />
        已通过绿色通道审核
      </p>
    </div>

    <div v-else>
      <div id="Reportinfo">
        <div id="reportntc">
          <p id="addr-time">四川师范大学绿色通道申请</p>
        </div>

        <div id="container">
          <div id="prewrite" class="form-container">
            <div class="form-group">
              <label for="povertyType" class="label">贫困类型：</label>
              <el-select v-model="form.povertyType" placeholder="请选择贫困类型">
                <el-option label="低保" value="低保" />
                <el-option label="建档立卡" value="建档立卡" />
                <el-option label="家庭变故" value="家庭变故" />
                <el-option label="其他（理由中说明）" value="其他" />
              </el-select>
            </div>

            <div class="form-group">
              <label for="povertyProof" class="label">贫困证明照片：</label>
              <input type="file" @change="handleFileChange" />
            </div>

            <div class="form-group">
              <label for="feeAmount" class="label">缓缴学费金额：</label>
              <el-input
                v-model.number="form.feeAmount"
                placeholder="请输入缓缴学费金额"
                suffix="元"
              />
            </div>
            <div class="form-group">
              <label for="reason" class="label">申请理由：</label>
              <div style="height: 150px; width: 100%">
                <textarea
                  v-model="form.reason"
                  placeholder="请输入申请理由"
                  style="width: 100%; height: 100%; box-sizing: border-box"
                ></textarea>
              </div>
            </div>

            <div class="submit-btns">
              <el-button
                type="primary"
                id="submitbtns"
                @click="submitForm"
                icon="el-icon-circle-check"
              >
                提交申请
              </el-button>
            </div>

            <p v-if="statu === 3" class="status-message">
              <img src="@/assets/failed.png" alt="失败" class="status-icon" />
              被打回，审核失败请重新提交
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { quillEditor } from "vue-quill-editor";

export default {
  components: {
    quillEditor,
  },
  data() {
    return {
      form: {
        povertyType: "",
        povertyProof: null,
        feeAmount: null,
        reason: "",
      },
      userInfo: {},
      statu: 0,
      editorOptions: {
        modules: {
          toolbar: [
            ["bold", "italic", "underline", "strike"],
            ["blockquote", "code-block"],
            [{ header: 1 }, { header: 2 }],
            [{ list: "ordered" }, { list: "bullet" }],
            [{ script: "sub" }, { script: "super" }],
            [{ indent: "-1" }, { indent: "+1" }],
            [{ direction: "rtl" }],
            [{ size: ["small", false, "large", "huge"] }],
            [{ color: [] }, { background: [] }],
            [{ font: [] }],
            [{ align: [] }],
            ["clean"],
          ],
        },
      },
    };
  },
  created() {
    this.getUserInfo().then(() => {
      this.fetchstatuFromBackend();
    });
  },
  methods: {
    getUserInfo() {
      return this.$axios.get("/sys/userInfo").then((res) => {
        this.userInfo = res.data.data || {};
      });
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.getBase64(file, (base64) => {
          this.form.povertyProof = base64;
        });
      }
    },

    getBase64(file, callback) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => callback(reader.result);
    },
    submitForm() {
      if (this.form.povertyType && this.form.feeAmount && this.form.reason) {
        // 添加对申请理由长度的判断
        if (this.form.reason.length > 1000) {
          this.$message.error("申请理由不能超过1000字！");
          return;
        }
        const base64String = this.form.povertyProof.split(",")[1];
        const requestData = {
          userId: this.userInfo.id || "",
          povertyType: this.form.povertyType,
          povertyProof: base64String || "",
          feeAmount: this.form.feeAmount,
          reason: this.form.reason,
          statu: "1", // 重置审核状态
        };
        this.$axios
          .post("/sys/channel/save", requestData)
          .then((response) => {
            this.$message.success("申请提交成功！");
            this.fetchstatuFromBackend();
          })
          .catch((error) => {
            console.error("申请提交失败:", error);
            this.$message.error("申请提交失败，请重试。");
          });
      } else {
        this.$message.error("请完整填写申请信息！");
      }
    },
    fetchstatuFromBackend() {
      // 确保在继续之前已经更新了this.userInfo
      console.log(this.userInfo.id);
      this.$axios
        .post("/sys/channel/info", this.userInfo.id)
        .then((response) => {
          this.statu = response.data.data.statu;
          // console.log(response.data.data.statu);
        })
        .catch((error) => {
          console.error("获取状态码时发生错误:", error);
        });
    },
  },
};
</script>
<style scoped>
#container1 {
  max-width: 1200px; /* Set your desired maximum width */
  margin: 0 auto; /* Center the container */
}

.status-container {
  text-align: center;
  margin-top: 80px;
}

.status-image,
.status-icon {
  width: 50px;
  height: 50px;
  margin: 0 auto;
  display: inline-block;
}

.status-text,
.status-message {
  color: green;
  font-weight: bold;
  font-size: 20px;
  text-align: center;
}

.status-icon {
  vertical-align: middle;
  margin-right: 8px;
}

#Reportinfo {
  width: 100%;
  background-color: #f0f4f2;
  padding: 20px;
  box-sizing: border-box;
}

#reportntc {
  background-color: #275c51;
  color: #fff;
  padding: 10px;
}

#container {
  width: 100%;
  background-color: #fff;
  padding: 20px;
  box-sizing: border-box;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.form-container {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
}

.label {
  width: 180px;
  font-size: 16px;
  color: #333;
}

.preview-container {
  margin-top: 10px;
  display: flex;
  align-items: center;
}

.preview-container img {
  max-width: 100%;
  max-height: 150px;
  margin-right: 10px;
}

.file-name {
  font-size: 14px;
}

.submit-btns {
  text-align: center;
  margin-top: 20px;
}

#submitbtns {
  width: 120px;
  height: 40px;
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
}
</style>
