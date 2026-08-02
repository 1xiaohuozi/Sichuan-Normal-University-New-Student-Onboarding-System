<template>
  <div>
    <!-- 头部 -->
    <div class="header">
      <!-- 搜索框 -->
      <el-input
        v-model="searchUsername"
        placeholder="请输入学号"
        @input="handleSearch"
        clearable
        style="width: 230px; margin-right: 10px"
        :rules="[{ validator: validateAlphanumeric1, trigger: 'blur' }]"
      ></el-input>

      <!-- 刷新按钮 -->
      <el-button type="primary" @click="fetchData" class="el-icon-refresh"
        >刷新</el-button
      >
    </div>

    <!-- 贫困信息列表 -->
    <el-table
      :data="povertyList"
      style="width: 100%; border: 1px solid #ebeef5"
      :stripe="true"
      border
    >
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="username" label="学号"></el-table-column>
      <el-table-column prop="created" label="创建时间">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.created) }}
        </template>
      </el-table-column>
      <el-table-column prop="povertyType" label="贫困类型"></el-table-column>
      <el-table-column prop="povertyProof" label="贫困证明照片">
        <template slot-scope="scope">
          <el-image :src="getImageUrl(scope.row.povertyProof)" fit="contain"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="feeAmount" label="缓缴学费金额"></el-table-column>
      <!-- <el-table-column prop="reason" label="申请理由"></el-table-column> -->
      <el-table-column prop="statu" label="状态">
        <template slot-scope="scope">
          <div v-html="formatstatu(scope.row.statu)"></div>
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
          <el-button type="text" @click="handleViewReason(scope.row)">查看理由</el-button>
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

    <!-- 分页-->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[5, 10, 20, 50]"
      :current-page="current"
      :page-size="size"
      :total="total"
      class="pagination"
    >
    </el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      povertyList: [], // 贫困信息数据
      searchUsername: "", // 搜索用户名
      total: 0,
      size: 10,
      current: 1,
      detailVisible: false, // 控制查看详情弹窗的显示与隐藏
      detailImages: [], // 存储需要查看的贫困证明照片
      reasonContent: "", // 用于存储理由内容
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
        validateAlphanumeric1(rule, value, callback) {
      const regExp = /^[A-Za-z0-9]+$/;
      const unsafeKeywords = [
        "SELECT",
        "select",
        "INSERT",
        "insert",
        "DELETE",
        "delete",
        "DROP",
        "drop",
        "UPDATE",
        "update",
      ]; // 可能引发安全问题的SQL关键词

      if (!regExp.test(value)) {
        callback(new Error("只能包含数字和字母"));
      } else if (unsafeKeywords.some((keyword) => value.includes(keyword))) {
        callback(new Error("包含不安全的关键词"));
      } else {
        callback();
      }
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

    formatstatu(statu) {
      if (statu === 1) {
        return '<span style="color: #e5e619; font-weight: bold;">审核中</span>';
      } else if (statu === 2) {
        return '<span style="color: green; font-weight: bold;">已通过</span>';
      } else if (statu === 3) {
        return '<span style="color: red; font-weight: bold;">被打回，等待重新上传</span>';
      } else {
        return ""; // 如果有其他状态，可以根据需要进行处理
      }
    },

    fetchData() {
      // 根据当前页和每页大小获取贫困信息数据
      this.$axios
        .get("/sys/channel/list", {
          params: {
            username: this.searchUsername,
            current: this.current,
            size: this.size,
          },
        })
        .then((response) => {
          this.povertyList = response.data.data.records;
          this.total = response.data.data.total;
        })
        .catch((error) => {
          console.error("获取数据时出错:", error);
        });
    },
    handleSearch() {
      // 根据用户名过滤数据
      this.fetchData();
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.size = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.current = val;
      this.fetchData();
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
            .post("/sys/channel/status", row.userId)
            .then((response) => {
              // 处理成功，你可能想要更新本地数据或重新加载整个数据
              this.$message.success("审核通过成功");
              this.fetchData(); // 审核通过后重新加载数据
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
            .post("/sys/channel/nopass", row.userId)
            .then((response) => {
              // 处理成功，你可能想要更新本地数据或重新加载整个数据
              this.$message.success("打回成功");
              this.fetchData(); // 打回后重新加载数据
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
      // 将需要查看的贫困证明照片赋值给 detailImages
      this.detailImages = [row.povertyProof];
      // 显示查看详情弹窗
      this.detailVisible = true;
    },
    handleViewReason(row) {
      // 将理由内容赋值给 reasonContent
      this.reasonContent = row.reason;
      // 调用弹窗展示理由内容
      this.showReasonDialog();
    },
    showReasonDialog() {
      // 使用弹窗组件，展示理由内容
      this.$alert(this.reasonContent, "申请理由", {
        dangerouslyUseHTMLString: true,
      });
    },
  },
};
</script>

<style>
.pagination {
  float: right;
  margin-top: 10px;
}
</style>
