const demo = {
  zz: () => Promise.resolve(import('./zz/ZComponents.vue')),
  vxeTable: () => Promise.resolve(import('./table/VxeTable.vue')),
  cropper: () => Promise.resolve(import('./comp/cropper/index.vue')),
  tinymce: () => Promise.resolve(import('./editor/tinymce/index.vue')),
  upload: () => Promise.resolve(import('./comp/upload/index.vue')),
  modal: () => Promise.resolve(import('./comp/modal/index.vue')),
};

export default demo;
