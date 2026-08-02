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
        <el-col :span="1"></el-col>
        <!-- 插入空白元素 -->
        <el-col :span="1" style="margin-left: 10px">
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
        <el-table-column prop="name" label="姓名" width="80"></el-table-column>
        <el-table-column prop="institute" label="学院"></el-table-column>
        <el-table-column prop="speciality" label="专业"></el-table-column>
        <el-table-column prop="classNumber" label="班级"></el-table-column>
        <el-table-column prop="campus" label="校区"></el-table-column>
        <el-table-column prop="region" label="区域"></el-table-column>
        <el-table-column prop="dormitory" label="宿舍楼"></el-table-column>
        <el-table-column prop="dormitoryNumber" label="宿舍号"></el-table-column>
        <el-table-column prop="bedNumber" label="床号"></el-table-column>
        <el-table-column prop="created" label="创建时间"></el-table-column>

        <el-table-column prop="created" width="200" label="创建时间"> </el-table-column>

        <el-table-column prop="icon" width="260px" label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-divider direction="vertical"></el-divider>

            <template>
              <el-popconfirm
                title="这是一段内容确定删除吗？"
                @confirm="handleDelete(scope.row.id)"
              >
                <el-button
                  type="text"
                  slot="reference"
                  class="el-icon-delete"
                  style="color: red"
                  >删除</el-button
                >
              </el-popconfirm>
            </template>
          </template>
        </el-table-column>
      </el-table>

      <br />
      <!-- 分页条 -->
      <div style="text-align: right; margin-top: 10px">
        <el-pagination
          background
          layout="total,sizes,prev, pager, next,jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[1, 2, 5, 10]"
          :page-size="pageSize"
          :current-page="currentPage"
        >
        </el-pagination>
      </div>

      <!-- 编辑弹窗 -->
      <!-- 编辑弹窗 -->
      <el-dialog
        title="编辑信息"
        :visible.sync="editDialogVisible"
        width="30%"
        @close="clearSelectedRow"
      >
        <!-- 只有在 selectedRow 不为 null 时渲染表单内容 -->
        <el-form v-if="selectedRow" :model="selectedRow" label-width="80px">
          <el-form-item label="校区" prop="campus">
            <el-input v-model="selectedRow.campus"></el-input>
          </el-form-item>
          <el-form-item label="区域" prop="region">
            <el-input v-model="selectedRow.region"></el-input>
          </el-form-item>
          <el-form-item label="宿舍楼" prop="dormitory">
            <el-input v-model="selectedRow.dormitory"></el-input>
          </el-form-item>
          <el-form-item label="宿舍号" prop="dormitoryNumber">
            <el-input v-model="selectedRow.dormitoryNumber"></el-input>
          </el-form-item>
          <el-form-item label="床号" prop="bedNumber">
            <el-input v-model="selectedRow.bedNumber"></el-input>
          </el-form-item>

          <!-- 其他表单项 -->

          <el-form-item>
            <el-button type="primary" @click="handleEditSave">保存</el-button>
            <el-button @click="editDialogVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
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
      pageSize: 5, // 一页展示的条数
      inputContent: "", // 搜索的数据值
      selectedRow: null,
      editDialogVisible: false,
    };
  },
  methods: {
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
    handleEdit(row) {
      this.selectedRow = { ...row };
      console.log(this.selectedRow);
      this.editDialogVisible = true;
    },
    handleEditSave() {
      const updateData = {
        id: this.selectedRow.id,
        campus: this.selectedRow.campus,
        region: this.selectedRow.region,
        dormitory: this.selectedRow.dormitory,
        dormitoryNumber: this.selectedRow.dormitoryNumber,
        bedNumber: this.selectedRow.bedNumber,
        // 根据需要添加其他属性
      };
      console.log(updateData.id);
      // 发送请求更新数据
      this.$axios
        .post("/sys/dormitory/update", updateData)
        .then((response) => {
          this.$message.success(response.data.msg);
          this.editDialogVisible = false; // 关闭弹窗
          this.getlist(); // 刷新数据
        })
        .catch((error) => {
          console.error("编辑失败:", error);
          // 根据需要处理错误
          this.$message.error("编辑失败，请重试");
        });
    },

    handleDelete(id) {
      this.$axios
        .post("/sys/dormitory/delete", id)
        .then((response) => {
          this.$message.success(response.data.msg);
          this.getlist(); // 刷新数据
        })
        .catch((error) => {
          console.error("删除失败:", error);
        });
    },
    clearSelectedRow() {
      this.selectedRow = null;
    },
    getlist() {
      axios.get("/sys/dormitory/list").then((result) => {
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
.search-row {
  margin-bottom: 10px; /* 调整底部外边距 */
}
</style>
