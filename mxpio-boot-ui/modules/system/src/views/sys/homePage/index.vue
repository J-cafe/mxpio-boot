<template>
  <div class="homePage">
    <div class="header">
      <div class="logo">
        <img :src="logoUrl" style="width: 200px" />
      </div>
      <div class="header_title">{{ systemDesc }}</div>
      <div class="operation">
        <a-dropdown>
          <div class="header-avatar" style="cursor: pointer">
            <span class="name">欢迎您:&nbsp;&nbsp;{{ userInfo.nickname }}</span>
            <DownOutlined class="ml-2" style="line-height: 20px" />
          </div>
          <template #overlay>
            <a-menu class="avatar-menu">
              <a-menu-item @click="toCenter">
                <UserOutlined />
                <span>个人中心</span>
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item @click="logout">
                <PoweroffOutlined style="margin-right: 8px" />
                <span>退出登录</span>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </div>
    <div class="banner">
      <a-carousel arrows :autoplay="false">
        <template #prevArrow>
          <div class="custom-slick-arrow" style="z-index: 1; left: 60px">
            <img :src="leftArrowImg" alt="" />
          </div>
        </template>
        <template #nextArrow>
          <div class="custom-slick-arrow" style="right: 60px">
            <img :src="rightArrowImg" alt="" />
          </div>
        </template>
        <div v-for="(page, pageIndex) in paginatedItems" :key="pageIndex" class="carousel-page">
          <div class="items-container">
            <div
              v-for="(item, itemIndex) in page"
              :key="itemIndex"
              :class="item.select ? 'carousel-item-select' : 'carousel-item'"
              @click="handleClickItem(item)"
            >
              <p class="title">{{ item.title }}</p>
              <p class="ltitle">模块</p>
              <p class="desc">{{ item.desc }}</p>
              <p class="goLook">前往查看</p>
            </div>
          </div>
        </div>
      </a-carousel>
    </div>
    <div class="footer">
      <template v-for="(item, index) in footItemList">
        <div v-if="index <= 7" :key="index" class="footItem" @click="handleFootClick(item)">
          <Icon :icon="item.icon" class="icon" :style="{ fontSize: '30px', color: '#fff' }" />
          <!-- <span>{{ item.icon }}</span> -->
          <img :src="item.select ? item.onImg : item.img" alt="" />
          <p :class="item.select ? 'on' : ''">{{ item.title }}</p>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import { Modal, Carousel as ACarousel, Dropdown as ADropdown } from 'ant-design-vue';
  import { DownOutlined, UserOutlined, PoweroffOutlined } from '@ant-design/icons-vue';
  import { useUserStore, usePermissionStore, useSystemStore } from '@mxpio/stores';
  import type { Menu as MenuType } from '@mxpio/types';
  import { useBridge } from '@mxpio/bridge';
  import { Icon } from '@mxpio/components';
  // Store
  const userStore = useUserStore();
  const permissionStore = usePermissionStore();
  const systemStore = useSystemStore();
  const { env } = useBridge();
  // Router
  const router = useRouter();

  // 响应式数据
  const itemList = ref<
    Array<{
      title: string;
      desc: string;
      select: boolean;
      key: string;
    }>
  >([]);

  const footItemList = ref<
    Array<{
      title: string;
      icon?: any;
      key: string;
      select: boolean;
      img: string;
      onImg: string;
      children?: Array<MenuType>;
    }>
  >([]);

  const screenWidth = ref(window.innerWidth);

  // 图片资源
  const leftArrowImg = new URL('../../../assets/images/home/leftJt.png', import.meta.url).href;
  const rightArrowImg = new URL('../../../assets/images/home/rightJt.png', import.meta.url).href;
  const platformBg = new URL('../../../assets/images/home/platform_bg.png', import.meta.url).href;
  const platformBgOn = new URL('../../../assets/images/home/platform_bg_on.png', import.meta.url)
    .href;
  const useImg = new URL('../../../assets/images/home/use.png', import.meta.url).href;
  const useOnImg = new URL('../../../assets/images/home/use-On.png', import.meta.url).href;
  const logoBase = new URL('../../../assets/images/logo.png', import.meta.url).href;

  // 计算属性
  const userInfo = computed(() => userStore.getUserInfo);
  const backMenuList = computed(() => permissionStore.getBackMenuList);
  const systemDesc = computed(() => systemStore.appSystemDesc || '');

  const { VITE_GLOB_API_URL: apiUrl } = env;
  const logoUrl = computed(() => {
    if (systemStore.logo) {
      return apiUrl + systemStore.logo;
    }
    return logoBase;
  });

  // 分页显示的模块
  const paginatedItems = computed(() => {
    const itemsPerPage = screenWidth.value < 1560 ? 3 : 4;
    const pages: (typeof itemList.value)[] = [];
    for (let i = 0; i < itemList.value.length; i += itemsPerPage) {
      pages.push(itemList.value.slice(i, i + itemsPerPage));
    }
    return pages;
  });

  // 平台菜单（footer菜单）
  const platformMenu = computed(() => {
    return backMenuList.value
      .filter(
        (item: MenuType) => item.meta?.urlScope === 'platform' && item.meta?.hideMenu !== true,
      )
      .map((item: MenuType) => ({
        ...item,
        path: item.path,
        meta: item.meta,
      }));
  });

  // 最近使用的模块
  const latestModules = ref<
    Array<{
      title: string;
      desc: string;
      select: boolean;
      key: string;
    }>
  >([]);
  const currentPlatform = ref<string>('');

  // 方法
  const handleClickItem = (clickedItem: (typeof itemList.value)[number]) => {
    itemList.value.forEach((item) => {
      item.select = item === clickedItem;
    });
    setLatestModules(clickedItem);

    router.push({ path: clickedItem.key });
  };

  const setLatestModules = (module) => {
    const latestModule = latestModules.value;
    // 排除掉重复
    const isLatestModule = latestModule.find((item) => item.key === module.key);
    if (!isLatestModule) {
      // 最多存储10条
      if (latestModules.value.length >= 10) {
        latestModule.shift();
      }
      latestModule.push({
        ...module,
        select: false,
      });
      localStorage.setItem('latestModules', JSON.stringify(latestModule));
    }
  };

  const handleFootClick = (clickedItem: (typeof footItemList.value)[number]) => {
    footItemList.value.forEach((item) => {
      item.select = item === clickedItem;
    });
  };

  const getModulesMenu = (key: string) => {
    if (key === 'latestModules') {
      const latestModule = latestModules.value;
      if (latestModule) {
        itemList.value = latestModule;
      } else {
        itemList.value = [];
      }
    } else {
      itemList.value =
        footItemList?.value
          .find((item) => item.key === key)
          ?.children?.filter((item: MenuType) => item.meta?.hideMenu !== true)
          .map((item: MenuType) => ({
            title: item.name,
            desc: 'MODULE',
            select: false,
            key: item.path || '',
          })) || [];
    }
  };

  const getFootItemList = () => {
    let list = platformMenu.value.map((item: MenuType, i: number) => {
      return {
        title: item.name,
        icon: item.meta?.icon,
        key: item.path,
        select: i === 0,
        img: platformBg,
        onImg: platformBgOn,
        children: item.children,
      };
    });
    // 只保留前6个
    // list = list.slice(0, 6);
    list.push({
      title: '最近使用',
      icon: undefined,
      key: 'latestModules',
      select: false,
      img: useImg,
      onImg: useOnImg,
      children: [],
    });
    footItemList.value = list;
  };

  const logout = () => {
    Modal.confirm({
      iconType: 'warning',
      title: '退出登录',
      content: '确定要退出登录吗？',
      onOk: async () => {
        await userStore.logout(true);
      },
    });
  };

  const toCenter = () => {
    router.push('/account/center');
  };

  const handleResize = () => {
    screenWidth.value = window.innerWidth;
  };

  // 监听底部菜单变化
  watch(
    footItemList,
    () => {
      const current = footItemList.value.find((item) => item.select);
      if (!current) return;
      currentPlatform.value = current.key;
      getModulesMenu(current.key);
    },
    { deep: true },
  );

  // 生命周期
  onMounted(() => {
    getFootItemList();
    // 从本地存储加载最近使用的模块
    const savedLatestModules = localStorage.getItem('latestModules');
    if (savedLatestModules) {
      try {
        latestModules.value = JSON.parse(savedLatestModules);
      } catch (e) {
        console.error('Failed to parse latest modules', e);
      }
    }
    window.addEventListener('resize', handleResize);
  });

  onBeforeUnmount(() => {
    window.removeEventListener('resize', handleResize);
  });
</script>

<style lang="less">
  .homePage {
    .ant-dropdown-menu,
    .ant-dropdown {
      background-color: rgb(255 255 255 / 60%) !important;
    }
  }
</style>

<style lang="less" scoped>
  // 1280*1024分辨率适配
  @media screen and (max-width: 1560px) {
    #app .homePage {
      .header {
        .logo {
          left: 30px;
        }

        .header_title {
          width: 400px;
          font-size: 36px;
        }

        .operation {
          right: 30px;
        }
      }

      .banner {
        .items-container {
          width: 80%;
        }
      }

      .footer {
        bottom: 40px;
        margin-top: 60px;

        .footItem {
          width: 120px;
          margin-left: 60px;

          p {
            font-size: 16px;
          }
        }

        .icon {
          top: 8px;
          left: 43px;
          font-size: 24px;
        }
      }

      :deep(.ant-carousel .custom-slick-arrow) {
        width: 80px;
        height: 350px;
      }
    }
  }

  .homePage {
    height: 100vh;
    overflow: hidden;
    background: url('../../../assets/images/home/homeBackground.jpg');
    background-size: 100% 100%;

    .header {
      display: flex;
      position: absolute;
      top: 0;
      right: 0;
      left: 0;
      justify-content: space-between;
      height: 80px;

      .logo {
        position: relative;
        top: 20px;
        left: 60px;
      }

      .header_title {
        width: 500px;
        color: #fff;
        font-size: 45px;
        font-weight: bold;
        line-height: 1.5;
        text-align: center;
        text-shadow:
          0 0 2px @primary-color,
          0 0 4px @primary-color,
          0 0 6px @primary-color;
      }

      .operation {
        position: relative;
        top: 20px;
        right: 60px;

        img {
          display: inline-block;
          margin-top: -2px;
          margin-left: 5px;
        }
      }
    }

    .banner {
      margin-top: 200px;

      .items-container {
        display: flex;
        justify-content: space-around;
        width: 70%;
        margin: 0 auto;

        .carousel-item,
        .carousel-item-select {
          position: relative;
          box-sizing: border-box;
          width: 255px;
          height: 350px;
          margin-top: 20px;
          padding-top: 80px;
          background-size: 100% 100%;
          cursor: pointer;

          .title {
            margin: 0;
            padding: 0;
            font-size: 36px;
            font-weight: bold;
          }

          .ltitle {
            font-size: 32px;
            font-weight: bold;
          }

          .desc {
            position: absolute;
            bottom: 100px;
            width: 100%;
            font-size: 16px;
            text-align: center;
          }

          .goLook {
            position: absolute;
            bottom: -10px;
            width: 100%;
            color: white;
            font-size: 16px;
            text-align: center;
            cursor: pointer;
          }
        }

        .carousel-item {
          background: url('../../../assets/images/home/menuBg.png') no-repeat;
          background-size: 100% 100%;

          .title,
          .ltitle,
          .desc {
            color: #017ffb;
          }
        }

        .carousel-item-select {
          background: url('../../../assets/images/home/menuBgOn.png') no-repeat;
          background-size: 100% 100%;

          .title,
          .ltitle,
          .desc {
            color: #fa7e02;
          }
        }
      }
    }

    .footer {
      display: flex;
      position: absolute;
      bottom: 60px;
      justify-content: space-around;
      width: 100%;
      margin-top: 90px;

      .footItem {
        position: relative;
        width: 150px;
        // margin-left: 85px;
        cursor: pointer;

        img {
          width: 100%;
        }

        p {
          position: absolute;
          bottom: 8px;
          width: 100%;
          color: #017ffb;
          font-size: 20px;
          font-weight: bold;
          text-align: center;
        }

        p.on {
          color: #fa7e02;
        }
      }

      .footItem:nth-child(1) {
        margin-left: 0;
      }

      .icon {
        position: absolute;
        top: 11px;
        left: 55px;
        color: #fff;
        font-size: 30px;
      }
    }
  }

  :deep(.ant-carousel .slick-slide) {
    height: 410px;
    overflow: hidden;
    background: transparent;
    text-align: center;
  }

  :deep(.ant-carousel .custom-slick-arrow) {
    position: absolute;
    z-index: 1;
    top: 50%;
    align-content: center;
    width: 100px;
    height: 400px;
    margin-top: 10px;
    transform: translateY(-50%);
    opacity: 0.7;

    &::before {
      display: none;
    }

    &:hover {
      opacity: 1;
    }
  }

  :deep(.ant-carousel .slick-dots) {
    bottom: 10px;

    li {
      margin: 0 6px;

      button {
        width: 12px !important;
        height: 12px !important;
        border-radius: 50% !important;
        opacity: 0.6;
        background: url('../../../assets/images/home/nextPage.png') no-repeat !important;
        background-size: 100% 100%;

        &:hover {
          transform: scale(1.1);
          opacity: 0.8;
        }
      }

      &.slick-active button {
        transform: scale(1.2);
        opacity: 1;
        background: url('../../../assets/images/home/nowPage.png') no-repeat !important;
      }
    }
  }
</style>
