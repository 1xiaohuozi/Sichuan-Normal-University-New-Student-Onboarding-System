<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input
          v-model="searchForm.username"
          placeholder="学号/工号"
          :rules="[{ validator: validateAlphanumeric1, trigger: 'blur' }]"
          clearable
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="getUserList" class="el-icon-search">搜索</el-button>
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
      :data="tableData"
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
          <el-button type="text" @click="roleHandle(scope.row.id)">分配角色</el-button>
          <el-divider direction="vertical"></el-divider>

          <el-button type="text" @click="repassHandle(scope.row.id, scope.row.username)"
            >重置密码</el-button
          >
          <el-divider direction="vertical"></el-divider>

          <el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>

          <template>
            <el-popconfirm
              title="这是一段内容确定删除吗？"
              @confirm="delHandle(scope.row.id)"
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
    <!-- 分页-->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[1, 3, 5, 10]"
      :current-page="current"
      :page-size="size"
      :total="total"
    >
    </el-pagination>

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

    <!-- 分配权限对话框 -->
    <el-dialog title="分配角色" :visible.sync="roleDialogFormVisible" width="600px">
      <el-form :model="roleForm">
        <el-tree
          :data="roleTreeData"
          show-checkbox
          ref="roleTree"
          :check-strictly="checkStrictly"
          node-key="id"
          :default-expand-all="true"
          :props="defaultProps"
        >
        </el-tree>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitRoleHandle('roleForm')">确 定</el-button>
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
      size: 5,
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
    };
  },
  created() {
    this.getUserList();
    this.$axios.get("/sys/role/list").then((res) => {
      this.roleTreeData = res.data.data.records;
    });
  },
  watch: {
    dialogVisible: function () {
      this.$refs.multipleTable.clearSelection();
    },
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
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      console.log("勾选");
      console.log(val);
      this.multipleSelection = val;

      this.delBtlStatu = val.length == 0;
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.size = val;
      this.getUserList();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.current = val;
      this.getUserList();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
      this.editForm = {};
    },
    handleClose() {
      this.resetForm("editForm");
    },
    getUserList() {
      this.$axios
        .get("/sys/user/list", {
          params: {
            username: this.searchForm.username,
            current: this.current,
            size: this.size,
          },
        })
        .then((res) => {
          this.tableData = res.data.data.records;
          this.size = res.data.data.size;
          this.current = res.data.data.current;
          this.total = res.data.data.total;
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
              this.getUserList();
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
        this.getUserList();
      });
    },
    roleHandle(id) {
      this.multipleSelection = [];
      this.roleDialogFormVisible = true;
      this.$axios.get("/sys/user/info/" + id).then((res) => {
        this.roleForm = res.data.data;
        let roleIds = [];
        res.data.data.sysRoles.forEach((row) => {
          roleIds.push(row.id);
        });
        this.$refs.roleTree.setCheckedKeys(roleIds);
      });
    },
    submitRoleHandle(formName) {
      var roleIds = this.$refs.roleTree.getCheckedKeys();
      this.$axios.post("/sys/user/role/" + this.roleForm.id, roleIds).then((res) => {
        this.$message({
          showClose: true,
          message: "操作成功",
          type: "success",
        });
        this.roleDialogFormVisible = false;
        this.getUserList();
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
          this.getUserList();
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
