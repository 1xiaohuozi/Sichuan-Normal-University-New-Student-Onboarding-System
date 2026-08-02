<template>
  <div id="app">
    <div>
      <!-- 设计搜索区 -->
      <el-row class="search-row">
        <el-col :span="3">
          <el-input
            v-model="inputContent"
            class="searchinput"
            placeholder="输入姓名搜索"
            @change="searchput"
            clearable
          >
          </el-input>
        </el-col>
        <!-- 插入空白元素 -->
        <el-col :span="2" style="margin-left: 10px">
          <el-button @click="searchput" class="el-icon-search" type="primary"
            >搜索</el-button
          >
        </el-col>
      </el-row>
      <!-- 设计表格展示区 -->
      <el-table
        stripe
        :data="tempData"
        :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
        style="width: 100%"
        border
      >
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="username" label="学号"></el-table-column>
        <el-table-column prop="created" label="创建时间">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.created) }}
          </template>
        </el-table-column>

        <el-table-column label="支付凭证" align="center">
          <template slot-scope="scope">
            <el-image :src="getImageUrl(scope.row.paymentImage)" fit="contain"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="身份证照片" align="center">
          <template slot-scope="scope">
            <el-image :src="getImageUrl(scope.row.idCardImage)" fit="contain"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="statu" label="状态">
          <template slot-scope="scope">
            <div v-html="formatStatus(scope.row.statu)"></div>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleApprove(scope.row)"
              v-if="scope.row.statu === 3 || scope.row.statu === 1"
              >通过</el-button
            >
            <el-button
              type="text"
              @click="handleApprovenopass(scope.row)"
              v-if="scope.row.statu === 2 || scope.row.statu === 1"
              >打回</el-button
            >
            <el-button type="text" @click="handleView(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 查看详情弹窗 -->
      <el-dialog :visible.sync="detailVisible" title="查看详情" width="50%">
        <el-carousel height="500px" :interval="4000">
          <el-carousel-item v-for="(image, index) in detailImages" :key="index">
            <el-image :src="getImageUrl(image)" fit="contain"></el-image>
          </el-carousel-item>
        </el-carousel>
      </el-dialog>

      <br />
      <!-- 分页条 -->
      <div style="text-align: right; margin-top: 10px">
        <el-pagination
          background
          layout="total,sizes,prev, pager, next,jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[1, 2, 4, 10]"
          :page-size="pageSize"
          :current-page="currentPage"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "App",
  data() {
    return {
      tableData: [], // 表格源数据
      tempData: [], // 表格显示数据
      currentPage: 1, // 当前页
      total: 0, // 数据总数
      pageSize: 4, // 一页展示的条数
      inputContent: "", // 搜索的数据值
      detailVisible: false, // 控制查看详情弹窗的显示与隐藏
      detailImages: [], // 存储需要查看的凭证图片

      editDialogVisible: false,
      editForm: {
        arrivalTime: "",
        transportation: "",
        members: "",
        notes: "",
      },
    };
  },

  methods: {
    loadImage(src) {
      return new Promise((resolve, reject) => {
        const img = new Image();
        img.onload = () => resolve();
        img.onerror = () => reject();
        img.src = this.getImageUrl(src);
      });
    },
    formatDateTime(dateTime) {
      return dateTime
        ? new Date(
            dateTime[0],
            dateTime[1] - 1,
            dateTime[2],
            dateTime[3],
            dateTime[4],
            dateTime[5]
          ).toLocaleString()
        : "";
    },

    formatStatus(status) {
      if (status === 1) {
        return '<span style="color: #e5e619; font-weight: bold;">审核中</span>';
      } else if (status === 2) {
        return '<span style="color: green; font-weight: bold;">已通过</span>';
      } else if (status === 3) {
        return '<span style="color: red; font-weight: bold;">被打回，等待重新上传</span>';
      } else {
        return ""; // 如果有其他状态，可以根据需要进行处理
      }
    },
    // 将二进制流转换成 Base64 编码的图片 URL
    getImageUrl(base64Data) {
      return base64Data ? `data:image;base64,${base64Data}` : "";
    },
    handleApprove(row) {
      console.log("User ID:", row.userId);
      this.$confirm("确认审核通过吗？", "审核通过", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "success",
      })
        .then(() => {
          // 调用后端 API 进行审核
          this.$axios
            .post("/sys/payment/status", row.userId)
            .then((response) => {
              // 处理成功，你可能想要更新本地数据或重新加载整个数据
              this.$message.success("审核通过成功");
              this.getlist(); // 审核通过后重新加载数据
            })
            .catch((error) => {
              // 处理错误
              console.error("审核通过时出错:", error);
              this.$message.error("审核通过失败");
            });
        })
        .catch(() => {
          // 如果用户取消确认，则不执行任何操作
        });
    },
    handleApprovenopass(row) {
      console.log("User ID:", row.userId);
      this.$confirm("确认打回吗？", "打回", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "success",
      })
        .then(() => {
          // 调用后端 API 进行审核
          this.$axios
            .post("/sys/payment/nopass", row.userId)
            .then((response) => {
              // 处理成功，你可能想要更新本地数据或重新加载整个数据
              this.$message.success("打回成功");
              this.getlist(); // 审核通过后重新加载数据
            })
            .catch((error) => {
              // 处理错误
              console.error("打回时出错:", error);
              this.$message.error("打回失败");
            });
        })
        .catch(() => {
          // 如果用户取消确认，则不执行任何操作
        });
    },
    handleView(row) {
      // 将需要查看的凭证图片赋值给 detailImages
      this.detailImages = [row.paymentImage, row.idCardImage];
      // 显示查看详情弹窗
      this.detailVisible = true;
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.handleCurrentChange(1);
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.currentChangePage(this.tableData, val);
    },
    currentChangePage(list, currentPage) {
      let from = (currentPage - 1) * this.pageSize;
      let to = currentPage * this.pageSize;
      this.tempData = [];
      for (; from < to; from++) {
        if (list[from] != null) {
          this.tempData.push(list[from]);
        }
      }
    },
    searchput() {
      this.tempData = [];
      this.tempData = this.tableData.filter((item, index) => {
        return item.name.includes(this.inputContent);
      });
    },

    resetEditForm() {
      // 重置编辑表单并关闭对话框
      this.$refs.editForm.resetFields();
      this.editDialogVisible = false;
    },
    getlist() {
      axios.get("/sys/payment/list").then((result) => {
        this.tableData = result.data.data.records;
        this.tempData = result.data.data.records;
        this.total = result.data.data.total;
        this.handleCurrentChange(1);
      });
    },
  },
  mounted() {
    this.getlist();
  },
};
</script>

<style>
/* Add your styles here if needed */
</style>
