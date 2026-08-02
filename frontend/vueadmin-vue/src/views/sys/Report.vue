<template>
  <div>
    <!-- 头部 -->
    <div class="header">
      <!-- 搜索框 -->
      <el-input
        v-model="searchKeyword"
        placeholder="请输入关键字"
        @input="handleSearch"
        clearable
        style="width: 230px; margin-right: 10px"
      ></el-input>

      <!-- 创建公告按钮 -->
      <el-button type="primary" @click="handleCreate" class="el-icon-plus"
        >创建公告</el-button
      >
    </div>

    <!-- 公告列表 -->
    <el-table
      :data="filteredData"
      style="width: 100%; border: 1px solid #ebeef5"
      :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
      :stripe="true"
      border
    >
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="content" label="内容">
        <template slot-scope="scope">
          {{ truncateText(scope.row.content, 20) }}
        </template>
      </el-table-column>
      <el-table-column prop="created" label="创建时间">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.created) }}
        </template>
      </el-table-column>

      <el-table-column prop="updated" label="更新时间">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.updated) }}
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm
              title="确定删除吗？"
              @confirm="handleDelete(scope.row.id)"
              v-if="hasAuth('sys:notice:delete')"
            >
              <el-button
                type="text"
                slot="reference"
                class="el-icon-delete"
                style="color: red"
              >
                删除</el-button
              >
            </el-popconfirm>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 创建/编辑公告的弹窗 -->
    <el-dialog
      :visible.sync="createDialogVisible"
      :title="createForm.id ? '编辑公告' : '创建公告'"
      width="50%"
    >
      <!-- 弹窗内容 -->
      <div class="announcement-details">
        <el-form :model="createForm" label-width="80px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="createForm.title"></el-input>
          </el-form-item>
          <el-form-item label="作者" prop="author">
            <el-input v-model="createForm.author"></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <!-- 使用富文本编辑器 -->
            <quill-editor
              v-model="createForm.content"
              :options="editorOptions"
            ></quill-editor>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="saveNotice"
          v-if="hasAuth('sys:notice:save') || hasAuth('sys:notice:update')"
          >保存</el-button
        >
        <el-button type="primary" @click="createDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>

    <!-- 分页-->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[1, 3, 5, 10]"
      :current-page="current"
      :page-size="size"
      :total="total"
      class="pagination"
    >
    </el-pagination>
  </div>
</template>

<script>
import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.snow.css";

export default {
  components: {
    quillEditor,
  },
  data() {
    return {
      tableData: [], // 所有公告数据
      filteredData: [], // 根据搜索关键字过滤后的数据
      searchKeyword: "", // 搜索关键字
      createDialogVisible: false,
      createForm: {
        id: null,
        title: "",
        author: "",
        content: "",
      },
      total: 0,
      size: 5,
      current: 1,
      editorOptions: {
        modules: {
          toolbar: [
            ["bold", "italic", "underline", "strike"], // 加粗、斜体、下划线、删除线
            [{ header: [1, 2, 3, false] }], // 标题
            [{ size: ["small", false, "large", "huge"] }], // 字体大小
            ["clean"], // 清除格式
          ],
        },
        theme: "snow",
      },
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    formatDateTime(dateTimeArray) {
      // 如果 dateTimeArray 为假值，则返回 "未更新"
      if (!dateTimeArray || dateTimeArray.length !== 6) {
        return "未更新";
      }

      const [year, month, day, hour, minute, second] = dateTimeArray;
      const formattedDate = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
      return formattedDate;
    },

    fetchData() {
      // 根据当前页和每页大小获取公告数据
      this.$axios
        .get("/sys/notice/list", {
          params: {
            current: this.current,
            size: this.size,
          },
        })
        .then((response) => {
          this.tableData = response.data.data.records;
          this.total = response.data.data.total;
          // console.log(response.data.data.records);
          // this.filteredData = response.data.data.records;
          this.handleSearch(); // 初始加载时执行一次搜索，显示所有数据
        })
        .catch((error) => {
          console.error("获取数据时出错:", error);
        });
    },
    truncateText(text, maxLength) {
      if (text.length > maxLength) {
        return text.substring(0, maxLength) + "...";
      } else {
        return text;
      }
    },
    handleCreate() {
      this.createDialogVisible = true;
      this.createForm = {
        id: null,
        title: "",
        author: "",
        content: "",
      };
    },
    saveNotice() {
      // 保存/更新公告的逻辑

      // 过滤this.createForm.content中的双引号和制表符（\t）
      const filteredContent = this.createForm.content.replace(/"|[\t]/g, "");

      console.log("Content before sending:", filteredContent);

      const url = this.createForm.id ? "/sys/notice/update" : "/sys/notice/save";

      // 构建请求数据，将过滤后的内容添加到requestData中
      const requestData = {
        ...this.createForm,
        content: filteredContent,
      };

      this.$axios
        .post(url, requestData)
        .then((response) => {
          this.$message.success(response.data.msg);
          this.createDialogVisible = false;
          this.fetchData(); // 刷新数据
        })
        .catch((error) => {
          console.error("保存公告失败:", error);
        });
    },

    handleEdit(row) {
      this.createDialogVisible = true;
      this.createForm = {
        id: row.id,
        title: row.title,
        author: row.author,
        content: row.content,
      };
    },
    handleDelete(id) {
      this.$axios
        .post("/sys/notice/delete", [id])
        .then((response) => {
          this.$message.success(response.data.msg);
          this.fetchData();
        })
        .catch((error) => {
          console.error("删除失败:", error);
        });
    },
    handleSearch() {
      // 将关键字转换为小写，以进行大小写不敏感的搜索
      const keyword = this.searchKeyword.trim().toLowerCase();

      this.filteredData = this.tableData.filter((item) => {
        const titleMatch = item.title.toLowerCase().includes(keyword);

        return titleMatch;
      });
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
  },
};
</script>

<style>
.pagination {
  float: right;
  margin-top: 10px;
}
</style>
