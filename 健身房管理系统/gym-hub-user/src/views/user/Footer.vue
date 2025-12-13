<template>
    <span>Copyright © {{$projectName}}</span>
</template>

<style lang="scss" scoped>
span {
    font-size: 14px;
    color: #999;
    letter-spacing: 1px;
    font-weight: 500;

    &::before {
        content: '🏋️ ';
        margin-right: 4px;
    }

    &::after {
        content: ' 🏋️';
        margin-left: 4px;
    }
}
</style>