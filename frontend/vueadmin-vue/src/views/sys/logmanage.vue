<template>
  <div>
    <div>
      <el-input
        style="width: 200px"
        placeholder="查询模块"
        v-model="operation"
      ></el-input>
      <el-select style="margin: 0 5px" v-model="type">
        <el-option
          v-for="item in ['新增', '修改', '删除', '登录']"
          :key="item"
          :value="item"
          :label="item"
        ></el-option>
      </el-select>
      <el-input
        style="width: 200px"
        placeholder="查询操作人"
        v-model="optUser"
      ></el-input>
      <el-button
        type="primary"
        style="margin-left: 10px"
        @click="load(1)"
        class="el-icon-search"
        >查询</el-button
      >
      <el-button type="info" @click="reset" class="el-icon-delete">重置</el-button>
    </div>
    <!-- <div style="margin: 10px 0">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div> -->
    <el-table
      :data="tableData"
      stripe
      :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="operation" label="操作模块"></el-table-column>
      <el-table-column prop="type" label="操作类型">
        <template v-slot="scope">
          <el-tag type="primary" v-if="scope.row.type === '新增'">{{
            scope.row.type
          }}</el-tag>
          <el-tag type="info" v-if="scope.row.type === '修改'">{{
            scope.row.type
          }}</el-tag>
          <el-tag type="danger" v-if="scope.row.type === '删除'">{{
            scope.row.type
          }}</el-tag>
          <el-tag type="danger" v-if="scope.row.type === '批量删除'">{{
            scope.row.type
          }}</el-tag>
          <el-tag type="success" v-if="scope.row.type === '登录'">{{
            scope.row.type
          }}</el-tag>
          <el-tag type="success" v-if="scope.row.type === '注册'">{{
            scope.row.type
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="url" label="接口"> </el-table-column>

      <el-table-column prop="ip" label="操作人IP"></el-table-column>
      <el-table-column prop="user" label="操作人"></el-table-column>
      <el-table-column prop="time" label="操作时间"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="danger"
            plain
            @click="showDeleteConfirmation(scope.row.id)"
          >
            删除
          </el-button>
          <el-button size="mini" type="primary" plain @click="showLog(scope.row.info)"
            >日志溯源</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!--分页-->
    <div style="margin: 10px 0">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        layout="total, prev, pager, next, ->, jumper"
        :total="total"
        style="text-align: right"
      >
      </el-pagination>
    </div>
    <el-dialog :visible.sync="dialogVisible" title="修改日志" width="80%">
      <div style="max-height: 600px; overflow-y: auto">
        <el-row :gutter="20">
          <el-col :span="8">
            <strong>修改前：</strong>
            <el-table
              :data="getObjectKeyValuePairs(beforeParamsObject)"
              stripe
              style="width: 100%; border-radius: 8px"
            >
              <el-table-column prop="key" label="参数"></el-table-column>
              <el-table-column prop="value" label="值"></el-table-column>
            </el-table>
          </el-col>
          <el-col :span="8">
            <strong>修改后：</strong>
            <el-table
              :data="getObjectKeyValuePairs(afterParamsObject)"
              stripe
              style="width: 100%; border-radius: 8px"
            >
              <el-table-column prop="key" label="参数"></el-table-column>
              <el-table-column prop="value" label="值"></el-table-column>
            </el-table>
          </el-col>
          <el-col :span="8">
            <strong>参数对比：</strong>
            <el-table
              :data="getDiffKeyValuePairs(paramsDiff)"
              stripe
              style="width: 100%; border-radius: 8px"
            >
              <el-table-column prop="key" label="参数"></el-table-column>
              <el-table-column prop="value" label="对比"></el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import "element-ui/lib/theme-chalk/index.css";

export default {
  data() {
    return {
      operation: "",
      type: "",
      optUser: "",
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      selectedRows: [],
      info: "",
      dialogVisible: false, // Initialize dialog visibility state
      beforeParamsObject: {}, // Initialize objects to prevent reactivity issues
      afterParamsObject: {},
      paramsDiff: {},
    };
  },
  methods: {
    getTypeClass(type) {
      switch (type) {
        case "新增":
          return "el-tag-primary";
        case "修改":
          return "el-tag-info";
        case "删除":
          return "el-tag-danger";
        case "批量删除":
          return "el-tag-danger"; // You can customize this based on your needs
        case "登录":
          return "el-tag-success";
        case "注册":
          return "el-tag-success";
        default:
          return ""; // Default or additional cases can be handled as needed
      }
    },
    async load(page) {
      // 发送API请求以根据筛选条件获取数据
      try {
        const response = await this.$axios.get("/sys/logs/selectByPage", {
          params: {
            operation: this.operation,
            type: this.type,
            optUser: this.optUser,
            pageNum: page,
            pageSize: this.pageSize,
          },
        });
        // this.$message.success("查询日志成功");
        // 更新表格数据和总数
        this.tableData = response.data.data.records;
        this.total = response.data.data.total;
      } catch (error) {
        console.error("获取数据时发生错误：", error);
      }
    },
    reset() {
      // 重置筛选条件并重新加载数据
      this.operation = "";
      this.type = "";
      this.optUser = "";
      this.load(1);
    },
    async delBatch() {
      // 发送API请求以删除选定的项目
      try {
        const response = await this.$axios.delete("/sys/logs/delete/batch", {
          headers: {
            "Content-Type": "application/json",
          },
          data: {
            ids: this.selectedRows.map((row) => row.id),
          },
          transformRequest: [(data) => JSON.stringify(data)],
        });

        // 根据需要处理响应
        console.log("批量删除响应：", response);
        // 删除后重新加载数据
        this.load(this.pageNum);
      } catch (error) {
        console.error("删除项目时发生错误：", error);
      }
    },
    showDeleteConfirmation(id) {
      this.$confirm("确定删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 用户点击确定时触发的操作
          this.del(id);
        })
        .catch(() => {
          // 用户点击取消时触发的操作
        });
    },
    async del(id) {
      // 发送API请求以删除单个项目
      try {
        const response = await this.$axios.delete(`/sys/logs/delete/${id}`);

        // 根据需要处理响应
        console.log("删除响应：", response);
        // 删除后重新加载数据
        this.load(this.pageNum);
      } catch (error) {
        console.error("删除项目时发生错误：", error);
      }
    },
    handleSelectionChange(selection) {
      // 更新选定的行
      this.selectedRows = selection;
    },
    handleCurrentChange(page) {
      // 处理页面变更
      this.load(page);
    },
    showLog(info) {
      // 匹配括号内的内容，使用非贪婪模式
      const matchBefore = info.match(/(\w+)\(([^)]*)\)/);
      const matchAfter = info.match(/(\w+)\(([^)]*)\)/g);

      // 解析参数的函数
      const parseParams = (params) => {
        // 将参数按逗号分隔，去除首尾空格，并分割成键值对
        const keyValuePairs = params.split(",").map((pair) => pair.trim());
        const paramObject = {};

        // 将键值对存入对象
        keyValuePairs.forEach((pair) => {
          const [key, value] = pair.split("=").map((item) => item.trim());
          paramObject[key] = value;
        });

        return paramObject;
      };

      // 如果有匹配到修改前的内容，解析为对象；否则为空对象
      this.beforeParamsObject = matchBefore ? parseParams(matchBefore[2]) : {};
      // 如果有匹配到修改后的内容，解析为对象；否则为空对象
      this.afterParamsObject = {};

      if (matchAfter) {
        // 对匹配到的修改后内容进行字符串拆分，确保每个匹配项都是一个完整的对象
        this.afterParamsObject = matchAfter
          .map((match) => {
            const [, key, value] = match.match(/(\w+)\(([^)]*)\)/);
            return { ...parseParams(value) }; // 移除 key
          })
          .pop();
      }

      // 计算参数差异
      this.paramsDiff = this.diffParams(this.beforeParamsObject, this.afterParamsObject);

      // 显示对话框
      this.dialogVisible = true;
    },

    diffParams(before, after) {
      const diff = {};
      for (const key in before) {
        if (before[key] !== after[key]) {
          diff[key] = {
            before: before[key],
            after: after[key],
          };
        }
      }
      return diff;
    },
    getObjectKeyValuePairs(obj) {
      return Object.entries(obj).map(([key, value]) => ({ key, value }));
    },
    getDiffKeyValuePairs(diff) {
      return Object.entries(diff).map(([key, { before, after }]) => ({
        key,
        value: `${before} → ${after}`,
      }));
    },
  },
  mounted() {
    // 在组件挂载时加载初始数据
    this.load(this.pageNum);
  },
};
</script>

<style>
.el-tooltip__popper {
  max-width: 300px;
}
.el-tag-primary {
  color: #fff; /* Customize text color */
  background-color: #409eff; /* Customize background color */
}

.el-tag-info {
  color: #fff;
  background-color: #67c23a;
}

.el-tag-danger {
  color: #fff;
  background-color: #f56c6c;
}

.el-tag-success {
  color: #fff;
  background-color: #67c23a;
}
.el-table th,
.el-table td {
  text-align: center;
}

.el-table th:last-child,
.el-table td:last-child {
  text-align: left;
}

.el-table {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.el-table th,
.el-table td {
  border-bottom: 1px solid #e0e0e0;
}

.el-table-column--selection {
  width: 40px;
}

.el-button {
  border-radius: 4px;
}
</style>
