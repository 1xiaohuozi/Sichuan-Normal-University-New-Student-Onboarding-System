<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input v-model="searchForm.username" placeholder="学号/工号" clearable>
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="studentlist" class="el-icon-search">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button
          class="el-icon-plus"
          type="primary"
          @click="dialogVisible = true"
          v-if="hasAuth('sys:user:save')"
          >新增</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗？" @confirm="delHandle(null)">
          <el-button
            type="danger"
            slot="reference"
            :disabled="delBtlStatu"
            v-if="hasAuth('sys:user:delete')"
            class="el-icon-delete"
            >批量删除</el-button
          >
        </el-popconfirm>
      </el-form-item>
    </el-form>

    <el-table
      ref="multipleTable"
      :data="pagedData"
      tooltip-effect="dark"
      style="width: 100%"
      :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
      border
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>

      <el-table-column label="头像" width="50">
        <template slot-scope="scope">
          <el-avatar size="small" :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>

      <el-table-column prop="username" label="学号/工号" width="120"> </el-table-column>

      <el-table-column prop="name" label="姓名" width="120"> </el-table-column>
      <el-table-column prop="sex" label="性别" width="50"> </el-table-column>

      <el-table-column prop="code" label="职务" width="120">
        <template slot-scope="scope">
          <el-tag size="small" type="info" v-for="item in scope.row.sysRoles">{{
            item.name
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="institute" label="学院"> </el-table-column>
      <el-table-column prop="speciality" label="专业"> </el-table-column>
      <el-table-column prop="classNumber" label="班级"> </el-table-column>

      <el-table-column prop="email" label="邮箱"> </el-table-column>

      <el-table-column prop="phone" label="手机号"> </el-table-column>

      <el-table-column prop="city" label="住址"> </el-table-column>

      <el-table-column prop="statu" label="状态">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.statu === 1" type="success">正常</el-tag>
          <el-tag size="small" v-else-if="scope.row.statu === 0" type="danger"
            >禁用</el-tag
          >
        </template>
      </el-table-column>

      <el-table-column prop="created" width="200" label="创建时间"> </el-table-column>

      <el-table-column prop="icon" width="260px" label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="repassHandle(scope.row.id, scope.row.username)"
            >重置密码</el-button
          >
          <el-divider direction="vertical"></el-divider>

          <el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>

          <template>
            <el-popconfirm
              title="这是一段内容确定删除吗？"
              @confirm="confirmDelete(scope.row.id)"
            >
              <template v-slot:reference>
                <el-button type="text">点击删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页-->
    <!-- 分页 -->
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    />

    <!--新增对话框-->
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="600px"
      :before-close="handleClose"
    >
      <el-form :model="editForm" :rules="editFormRules" ref="editForm">
        <el-form-item label="学号/工号" prop="username" label-width="100px">
          <el-input v-model="editForm.username" autocomplete="off"></el-input>
          <el-alert
            title="初始密码为888888"
            :closable="false"
            type="info"
            style="line-height: 12px"
          ></el-alert>
        </el-form-item>

        <el-form-item label="姓名" prop="name" label-width="100px"
          ><el-input v-model="editForm.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="性别" prop="sex" label-width="100px">
          <el-select v-model="editForm.sex" placeholder="请选择性别" autocomplete="off">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="学院" prop="email" label-width="100px">
          <el-input v-model="editForm.institute" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="email" label-width="100px">
          <el-input v-model="editForm.email" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="手机号" prop="phone" label-width="100px">
          <el-input v-model="editForm.phone" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="住址" prop="city" label-width="100px">
          <el-input v-model="editForm.city" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="状态" prop="statu" label-width="100px">
          <el-radio-group v-model="editForm.statu">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('editForm')">取 消</el-button>
        <el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      searchForm: {},
      delBtlStatu: true,
      total: 0,
      size: 10,
      current: 1,
      dialogVisible: false,
      editForm: {},
      tableData: [],
      editFormRules: {
        username: [{ required: true, message: "请输入学号称", trigger: "blur" }],
        email: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
        statu: [{ required: true, message: "请选择状态", trigger: "blur" }],
      },
      multipleSelection: [],
      roleDialogFormVisible: false,
      defaultProps: {
        children: "children",
        label: "name",
      },
      roleForm: {},
      roleTreeData: [],
      treeCheckedKeys: [],
      checkStrictly: true,
      currentPage: 1,
      pageSize: 5,
      total: 0,
      tableData: [],
    };
  },
  created() {
    this.getUserInfo().then(() => {
      this.studentlist();
    });
  },
  watch: {
    dialogVisible: function () {
      this.$refs.multipleTable.clearSelection();
    },
  },
  mounted() {
    // 在组件挂载后，将 userInfo 的值赋给 editForm
    this.editForm.institute = this.userInfo.institute;
  },
  computed: {
    // 计算分页后的数据
    pagedData() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.tableData.slice(startIndex, endIndex);
    },
  },
  methods: {
    confirmDelete(id) {
      if (id !== 4) {
        this.delHandle(id);
      } else if (id == 4) {
        this.$message({
          showClose: true,
          message: "不能删除超级管理员",
          type: "warning",
        });
        return;
      }
    },
    handleSelectionChange(val) {
      console.log("勾选");
      console.log(val);
      this.multipleSelection = val;

      this.delBtlStatu = val.length == 0;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
    },

    getUserInfo() {
      return this.$axios.get("/sys/userDetailInfo").then((res) => {
        this.userInfo = res.data.data;
        console.log(this.userInfo.institute);
      });
    },

    studentlist() {
      console.log(this.userInfo.institute);
      this.$axios
        .post("/sys/user/branch/institute", this.userInfo.institute)

        .then((response) => {
          this.tableData = response.data.data.records;
          this.size = response.data.data.size;
          this.current = response.data.data.current;
          this.total = response.data.data.total;
        })
        .catch((error) => {
          console.error("获取数据时发生错误：", error);
        });
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios
            .post("/sys/user/" + (this.editForm.id ? "update" : "save"), this.editForm)
            .then((res) => {
              this.$message({
                showClose: true,
                message: "操作成功",
                type: "success",
              });
              this.dialogVisible = false;
              this.studentlist();
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    editHandle(id) {
      this.$axios.get("/sys/user/info/" + id).then((res) => {
        this.editForm = res.data.data;
        this.dialogVisible = true;
      });
    },
    delHandle(id) {
      var ids = [];
      if (id) {
        ids.push(id);
      } else {
        this.multipleSelection.forEach((row) => {
          ids.push(row.id);
        });
      }
      var idsJson = JSON.stringify(ids);
      this.$axios.post("/sys/user/delete", idsJson).then((res) => {
        this.$message({
          showClose: true,
          message: "操作成功",
          type: "success",
        });
        this.studentlist();
      });
    },
    repassHandle(id, username) {
      this.$confirm("将重置用户【" + username + "】的密码, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$axios.post("/sys/user/repass", id).then((res) => {
          this.$message({
            showClose: true,
            message: "操作成功",
            type: "success",
          });
          this.studentlist();
        });
      });
    },
  },
};
</script>

<style scoped>
.el-pagination {
  float: right;
  margin-top: 22px;
}
</style>
