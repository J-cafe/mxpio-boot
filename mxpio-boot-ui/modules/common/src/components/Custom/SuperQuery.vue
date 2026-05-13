<template>
  <BasicForm :disabled="disabled" @register="registerForm">
    <template #add="{ field }">
      <a-button @click="add" class="mr-2" :disabled="disabled">+</a-button>
      <a-button @click="() => del(field)" :disabled="disabled">-</a-button>
    </template>
  </BasicForm>
</template>
<script lang="ts" setup>
  import { ref, onMounted, watch, nextTick } from 'vue';
  import { BasicForm, useForm, FormSchema } from '@mxpio/components';

  defineOptions({ name: 'SuperQuery' });

  type FieldListType = {
    field: string;
    label: string;
    component: string;
    componentProps?: any; // 支持form组件的props
  };

  // fieldList 参数示例
  // fieldList =[
  // { component: 'Input', field: 'itemCode', label: '物料编码' },
  // { component: 'ItemGroupSelect', field: 'itemGroupCode', label: '物料组' },
  // {
  //   component: 'Select',
  //   field: 'aaa',
  //   label: '测试',
  //   componentProps: { options: [{ label: '测试', value: '1' }] },
  //  },
  // ]

  // criteria 参数示例
  // 示例
  // {
  //   criterions: [
  //  { type: 'AND',
  //       criterions: [
  //         {
  //           fieldName: 'itemCode',
  //           operator: 'EQ',
  //           value: '123',
  //         },
  //       ],
  //     },
  //   ],
  //   orders: [],
  // }
  const props = defineProps({
    fieldList: {
      type: Array as PropType<FieldListType[]>,
      default: () => [],
    },
    criteria: {
      // 查询条件
      type: Object as PropType<Recordable>,
      default: () => ({
        criterions: [
          {
            type: 'AND',
            criterions: [{}],
          },
        ],
        orders: [],
      }),
    },
    disabled: {
      // 是否禁用
      type: Boolean,
      default: false,
    },
  });

  const formSchema: FormSchema[] = [
    {
      field: 'matchType',
      label: '过滤条件匹配:',
      component: 'Select',
      componentProps: {
        options: [
          { label: 'AND（所有条件都要求匹配）', value: 'AND' },
          { label: 'OR（条件中的任意一个匹配）', value: 'OR' },
        ],
        style: {
          width: '300px',
        },
      },
      colProps: {
        span: 24,
      },
      defaultValue: 'AND',
    },
  ];

  const operatorOptions = [
    { label: '等于', value: 'EQ' },
    { label: '包含', value: 'LIKE' },
    { label: '不包含', value: 'NOT_LIKE' },
    { label: '开始是', value: 'LIKE_START' },
    { label: '结尾是', value: 'LIKE_END' },
    { label: '在...中', value: 'IN' },
    { label: '不等于', value: 'NE' },
    { label: '大于', value: 'GT' },
    { label: '大于等于', value: 'GE' },
    { label: '小于', value: 'LT' },
    { label: '小于等于', value: 'LE' },
  ];

  const fieldNameOptions = ref<{ label: string; value: string }[]>([]);
  const fieldMap = new Map();

  const [
    registerForm,
    {
      resetFields,
      resetSchema,
      updateSchema,
      validate,
      getFieldsValue,
      appendSchemaByField,
      removeSchemaByField,
    },
  ] = useForm({
    compact: true,
    schemas: formSchema,
    actionColOptions: { span: 24 },
    baseColProps: { span: 6 },
    showResetButton: false,
    showSubmitButton: false,
  });

  // 初始化表单
  function initFormSchema(criteria) {
    const { criterions } = criteria;
    const schemas: Recordable[] = [{ ...formSchema[0] }];
    clearFormSchema();
    criterions?.forEach((item: any) => {
      const { criterions } = item;
      criterions.forEach((item: any) => {
        const id = item.id || generateId();
        schemas.push(...getFormSchema(item, id));
      });
    });
    resetSchema(schemas);
    updateSchema(schemas);
  }

  // 清除表单
  function clearFormSchema() {
    let values = getFieldsValue();
    const keys = Object.keys(values);
    removeSchemaByField(keys);
  }

  // 手动新增插入的表单
  function getFormSchema(item, i) {
    const schemas = [
      {
        field: `fieldName_${i}`,
        component: 'Select',
        // componentProps: {
        //   options: fieldNameOptions,
        //   onChange: (value) => {
        //     const fieldName = value;
        //     updateSchema([
        //       {
        //         field: 'value_' + i,
        //         component: fieldMap.get(fieldName)?.component || 'Input',
        //         componentProps: fieldMap.get(fieldName)?.componentProps || {},
        //       },
        //     ]);
        //   },
        // },
        componentProps: ({ schema }) => {
          return {
            options: fieldNameOptions.value,
            onChange: (value) => {
              const { field } = schema || {};
              if (!field) {
                return;
              }
              const fieldId = field?.split('_')[1] || 0;
              const fieldName = value;
              updateSchema([
                {
                  field: 'value_' + fieldId,
                  component: fieldMap.get(fieldName)?.component || 'Input',
                  componentProps: fieldMap.get(fieldName)?.componentProps || {},
                },
              ]);
            },
          };
        },
        label: ' ',
        defaultValue: item.fieldName || null,
        colProps: {
          span: 8,
        },
      },
      {
        field: `operator_${i}`,
        component: 'Select',
        componentProps: {
          options: operatorOptions,
        },
        label: ' ',
        defaultValue: item.operator || null,
        colProps: {
          span: 4,
        },
      },
      {
        field: `value_${i}`,
        component: fieldMap.get(item.fieldName)?.component || 'Input',
        componentProps: fieldMap.get(item.fieldName)?.componentProps || {},
        label: ' ',
        defaultValue: item.value || null,
        colProps: {
          span: 8,
        },
      },
      {
        field: `${i}`,
        component: 'Input',
        label: ' ',
        slot: 'add',
        colProps: {
          span: 4,
        },
      },
    ];
    return schemas;
  }

  // 初始化fieldNameOptions
  function getfieldNameOptions() {
    props.fieldList.forEach((item: any) => {
      fieldNameOptions.value.push({
        label: item.label,
        value: item.field,
      });
      fieldMap.set(item.field, item);
    });
  }

  // 转换form对象为criteria对象
  function toCriteria(values) {
    // 遍历values，把form对象的值转换为criteria对象
    const criterions: Recordable = [];
    Object.keys(values).forEach((item: any) => {
      if (item.includes('fieldName_')) {
        const index = item.split('_')[1];
        const fieldName = values[`fieldName_${index}`];
        const operator = values[`operator_${index}`];
        const value = values[`value_${index}`];
        const criteria = {
          fieldName,
          operator,
          value,
        };
        criterions.push(criteria);
      }
    });
    return {
      criterions: [
        {
          type: values.matchType,
          criterions,
        },
      ],
      orders: [],
    };
  }

  function getData() {
    let values = getFieldsValue();
    const criteria = toCriteria(values);
    return criteria;
  }

  function add() {
    // n.value++;
    const id = generateId();
    appendSchemaByField(getFormSchema({}, id), '');
  }

  function del(field: string) {
    // 删除fieldName_${field}、operator_${field}、value_${field}、${field}
    removeSchemaByField([`fieldName_${field}`, `operator_${field}`, `value_${field}`, `${field}`]);
    // n.value--;
  }

  function generateId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2, 5);
  }

  watch(
    () => props.criteria,
    (newVal, oldVal) => {
      if (newVal !== oldVal) {
        nextTick(() => {
          initFormSchema(props.criteria);
        });
      }
    },
    {
      deep: true,
      immediate: true,
    },
  );

  onMounted(() => {
    resetFields();
    getfieldNameOptions();
  });

  defineExpose({
    getData,
    validate,
  });
</script>
