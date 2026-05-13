<template>
  <div>
    <vxe-grid
      ref="gridRef"
      v-bind="gridOptions"
      :style="{
        '--vxe-ui-table-border-color': borderColor,
        '--vxe-ui-table-border-radius': borderRadius,
      }"
      @filter-change="filterChangeEvent"
    >
      <template #name_filter="{ column }">
        <ADatePicker
          v-model="option.data"
          v-for="(option, index) in column.filters"
          :key="index"
          :getPopupContainer="getPopupContainer"
          @change="(e, value) => changeDate(e, value, option)"
        />
      </template>
      <template #sex="{ column }">
        <a-range-picker
          v-model="column.filters[0].data"
          v-for="(option, index) in column.filters"
          :key="index"
          :getPopupContainer="getPopupContainer"
          show-time
        />
      </template>
    </vxe-grid>
  </div>
</template>

<script>
  import { getPopupContainer } from '@mxpio/utils';
  import { DatePicker as ADatePicker, RangePicker as ARangePicker } from 'ant-design-vue';

  export default {
    components: {
      ADatePicker,
      ARangePicker,
    },
    data() {
      const tableData = [
        { id: 10001, name: 'Test1', role: 'Develop', sex: 'Man', age: 28, address: 'test abc' },
        { id: 10002, name: 'Test2', role: 'Test', sex: 'Women', age: 22, address: 'Guangzhou' },
        { id: 10003, name: 'Test3', role: 'PM', sex: 'Man', age: 32, address: 'Shanghai' },
        { id: 10004, name: 'Test4', role: 'Designer', sex: 'Women', age: 24, address: 'Shanghai' },
      ];
      return {
        tableData,
        gridOptions: {
          height: '1700px',
          border: true,
          columns: [
            { field: 'seq', type: 'seq', width: 70 },
            {
              field: 'name',
              title: 'Name',
              editRender: { name: 'AInput' },
              filterRender: {
                name: 'AInput',
              },
              filters: [{ data: '' }],
            },
            {
              field: 'sex',
              title: 'Sex',
              slots: {
                filter: 'sex',
              },
              filters: [{ data: '' }],
            },
            {
              field: 'age',
              title: 'Age',
              slots: {
                filter: 'name_filter',
              },
              filters: [{ data: '' }],
            },
          ],
          data: [
            { id: 10001, name: 'Test1', role: 'Develop', sex: 'Man', age: 28, address: 'test abc' },
            { id: 10002, name: 'Test2', role: 'Test', sex: 'Women', age: 22, address: 'Guangzhou' },
            { id: 10003, name: 'Test3', role: 'PM', sex: 'Man', age: 32, address: 'Shanghai' },
            {
              id: 10004,
              name: 'Test4',
              role: 'Designer',
              sex: 'Women',
              age: 24,
              address: 'Shanghai',
            },
          ],
          footerData: [{ seq: '合计', name: '777', sex: '333', age: '111' }],
          proxyConfig: {
            enabled: true,
            autoLoad: true,
            ajax: {
              query: async ({ page, form, sorts, filters }) => {
                console.log(page, form, sorts, filters);
                return {
                  list: [
                    {
                      id: 10001,
                      name: 'Test1',
                      role: 'Develop',
                      sex: 'Man',
                      age: 28,
                      address: 'test abc',
                    },
                    {
                      id: 10002,
                      name: 'Test2',
                      role: 'Test',
                      sex: 'Women',
                      age: 22,
                      address: 'Guangzhou',
                    },
                    {
                      id: 10003,
                      name: 'Test3',
                      role: 'PM',
                      sex: 'Man',
                      age: 32,
                      address: 'Shanghai',
                    },
                    {
                      id: 10004,
                      name: 'Test4',
                      role: 'Designer',
                      sex: 'Women',
                      age: 24,
                      address: 'Shanghai',
                    },
                  ],
                };
              },
            },
          },
          filterConfig: {
            enabled: true,
            remote: true,
          },
          toolbarConfig: {
            enabled: true,
            refresh: true,
            refreshOptions: {
              queryMethod: () => {
                this.$refs.gridRef.commitProxy('query');
              },
            },
          },
          floatingFilterConfig: {
            enabled: true,
          },
          editConfig: {
            trigger: 'click',
            mode: 'cell',
          },
        },
      };
    },
    methods: {
      resultEvent() {
        const $table = this.$refs.tableRef;
        if ($table) {
          const tableData = $table.getFullData();
          console.log(tableData);
        }
      },
      filterChangeEvent({ page, form, sorts, filters }) {
        console.log(page, form, sorts, filters);
      },
      getPopupContainer,
      changeDate(e, value, option) {
        option.data = e;
        console.log(e, value, option);
      },
    },
  };
</script>
