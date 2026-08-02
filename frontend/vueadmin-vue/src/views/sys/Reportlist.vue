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
        <el-table-column prop="name" label="姓名" width="80"></el-table-column>
        <el-table-column prop="username" label="学号"></el-table-column>
        <el-table-column prop="arrivalTime" label="到达时间"></el-table-column>
        <el-table-column prop="transportation" label="交通工具"></el-table-column>
        <el-table-column prop="members" label="成员数量"></el-table-column>
        <el-table-column prop="notes" label="车牌号"></el-table-column>
        <el-table-column prop="created" label="创建时间"></el-table-column>
        <el-table-column prop="icon" width="260px" label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row.id)">编辑</el-button>
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
      <el-dialog
        :visible.sync="editDialogVisible"
        title="编辑信息"
        width="30%"
        @close="resetEditForm"
      >
        <el-form
          :model="editForm"
          :rules="editFormRules"
          ref="editForm"
          label-width="80px"
        >
          <el-form-item label="到达时间" prop="arrivalTime">
            <el-input v-model="editForm.arrivalTime"></el-input>
          </el-form-item>
          <el-form-item label="交通工具" prop="transportation">
            <el-input v-model="editForm.transportation"></el-input>
          </el-form-item>
          <el-form-item label="成员数量" prop="members">
            <el-input v-model="editForm.members"></el-input>
          </el-form-item>
          <el-form-item label="车牌号" prop="notes">
            <el-input v-model="editForm.notes"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleEditSubmit">保存</el-button>
          </el-form-item>
        </el-form>
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
          :page-sizes="[1, 3, 5, 10]"
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
      pageSize: 5, // 一页展示的条数
      inputContent: "", // 搜索的数据值
      editDialogVisible: false,
      editForm: {
        arrivalTime: "",
        transportation: "",
        members: "",
        notes: "",
      },
      editFormRules: {
        arrivalTime: [{ required: true, message: "请输入到达时间", trigger: "blur" }],
        transportation: [{ required: true, message: "请输入交通工具", trigger: "blur" }],
        members: [{ required: true, message: "请输入成员数量", trigger: "blur" }],
        notes: [{ required: true, message: "请输入车牌号", trigger: "blur" }],
      },
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
    handleDelete(id) {
      // 在这里实现删除的逻辑
      // 发送请求删除数据，然后刷新表格数据
      this.$axios
        .post("/sys/report/delete", id)
        .then((response) => {
          this.$message.success(response.data.msg);
          this.getlist(); // 刷新数据
        })
        .catch((error) => {
          console.error("删除失败:", error);
        });
    },
    handleEdit(id) {
      // 使用id获取选定行的数据
      const selectedRow = this.tableData.find((row) => row.id === id);

      // 使用选定行的数据填充编辑表单
      this.editForm = {
        arrivalTime: selectedRow.arrivalTime,
        transportation: selectedRow.transportation,
        members: selectedRow.members,
        notes: selectedRow.notes,
      };

      // 显示编辑对话框
      this.editDialogVisible = true;
    },
    handleEditSubmit() {
      // 验证表单
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          // 发送更新数据的请求
          const id = this.tableData[this.currentPage].id; // 根据您的数据结构调整此逻辑
          const updateData = {
            id: id,
            arrivalTime: this.editForm.arrivalTime,
            transportation: this.editForm.transportation,
            members: this.editForm.members,
            notes: this.editForm.notes,
          };

          axios
            .post(`/sys/report/update`, updateData)
            .then((response) => {
              this.$message.success(response.data.msg);
              this.getlist(); // 刷新数据
              this.editDialogVisible = false; // 关闭对话框
            })
            .catch((error) => {
              console.error("更新失败:", error);
            });
        }
      });
    },
    resetEditForm() {
      // 重置编辑表单并关闭对话框
      this.$refs.editForm.resetFields();
      this.editDialogVisible = false;
    },
    getlist() {
      axios.get("/sys/report/list").then((result) => {
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
