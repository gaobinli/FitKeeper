<template>
    <div class="rich-editor-wrapper">
        <div class="editor-toolbar">
            <button
                v-for="tool in tools"
                :key="tool.name"
                @click="execCommand(tool.name)"
                :class="['tool-btn', { active: isCommandActive(tool.name) }]"
                :title="tool.title"
            >
                {{ tool.label }}
            </button>
            <div class="divider"></div>
            <button class="tool-btn" @click="insertImage" title="插入图片">图片</button>
            <button class="tool-btn" @click="insertVideo" title="插入视频">视频</button>
        </div>
        <input
            ref="imageInput"
            type="file"
            accept="image/*"
            style="display: none"
            @change="handleImageUpload"
        />
        <input
            ref="videoInput"
            type="file"
            accept="video/*"
            style="display: none"
            @change="handleVideoUpload"
        />
        <div
            ref="editorContainer"
            class="editor-content"
            contenteditable="true"
            @input="updateContent"
            @paste="handlePaste"
        ></div>
    </div>
</template>

<script>
import { ref, defineProps, defineEmits, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { post } from '@/api/http'

export default {
    name: 'RichTextEditor',
    props: {
        modelValue: {
            type: String,
            default: ''
        }
    },
    emits: ['update:modelValue'],
    setup(props, { emit }) {
        const editorContainer = ref()
        const imageInput = ref()
        const videoInput = ref()

        const tools = [
            { name: 'bold', label: '粗体', title: '粗体' },
            { name: 'italic', label: '斜体', title: '斜体' },
            { name: 'underline', label: '下划线', title: '下划线' },
            { name: 'strikethrough', label: '删除线', title: '删除线' },
            { name: 'createLink', label: '链接', title: '插入链接' },
            { name: 'unlink', label: '移除链接', title: '移除链接' },
            { name: 'insertUnorderedList', label: '列表', title: '无序列表' },
            { name: 'insertOrderedList', label: '有序列表', title: '有序列表' },
            { name: 'formatBlock', label: '标题', title: '标题' },
        ]

        const execCommand = (command) => {
            document.execCommand(command, false, null)
            editorContainer.value?.focus()
        }

        const isCommandActive = (command) => {
            return document.queryCommandState(command)
        }

        const updateContent = () => {
            if (editorContainer.value) {
                emit('update:modelValue', editorContainer.value.innerHTML)
            }
        }

        const insertImage = () => {
            imageInput.value?.click()
        }

        const insertVideo = () => {
            videoInput.value?.click()
        }

        const handleImageUpload = async (event) => {
            const file = event.target.files?.[0]
            if (!file) return

            const formData = new FormData()
            formData.append('file', file)

            try {
                const response = await post('upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                })

                let imageUrl = response
                if (typeof response === 'object') {
                    imageUrl = response.url || response.data?.url || response
                }

                if (editorContainer.value) {
                    const img = document.createElement('img')
                    img.src = imageUrl
                    img.style.maxWidth = '100%'
                    img.style.height = 'auto'
                    img.style.margin = '10px 0'
                    editorContainer.value.appendChild(img)
                    updateContent()
                }

                ElMessage({
                    message: '图片上传成功',
                    type: 'success',
                })
            } catch (error) {
                ElMessage({
                    message: '图片上传失败',
                    type: 'error',
                })
            }
            event.target.value = ''
        }

        const handleVideoUpload = async (event) => {
            const file = event.target.files?.[0]
            if (!file) return

            const formData = new FormData()
            formData.append('file', file)

            try {
                const response = await post('upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                })

                let videoUrl = response
                if (typeof response === 'object') {
                    videoUrl = response.url || response.data?.url || response
                }

                if (editorContainer.value) {
                    const video = document.createElement('video')
                    video.src = videoUrl
                    video.controls = true
                    video.style.maxWidth = '100%'
                    video.style.height = 'auto'
                    video.style.margin = '10px 0'
                    editorContainer.value.appendChild(video)
                    updateContent()
                }

                ElMessage({
                    message: '视频上传成功',
                    type: 'success',
                })
            } catch (error) {
                ElMessage({
                    message: '视频上传失败',
                    type: 'error',
                })
            }
            event.target.value = ''
        }

        const handlePaste = (event) => {
            const items = event.clipboardData?.items
            if (!items) return

            for (let i = 0; i < items.length; i++) {
                const item = items[i]
                if (item.type.includes('image')) {
                    const file = item.getAsFile()
                    const input = imageInput.value
                    const dataTransfer = new DataTransfer()
                    dataTransfer.items.add(file)
                    input.files = dataTransfer.files
                    handleImageUpload({ target: input })
                }
            }
        }

        // 初始化编辑器内容
        watch(
            () => props.modelValue,
            (newValue) => {
                if (editorContainer.value && editorContainer.value.innerHTML !== newValue) {
                    editorContainer.value.innerHTML = newValue || ''
                }
            }
        )

        return {
            editorContainer,
            imageInput,
            videoInput,
            tools,
            execCommand,
            isCommandActive,
            updateContent,
            insertImage,
            insertVideo,
            handleImageUpload,
            handleVideoUpload,
            handlePaste
        }
    }
}
</script>

<style scoped lang="scss">
.rich-editor-wrapper {
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    overflow: hidden;
    background-color: white;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.editor-toolbar {
    display: flex;
    align-items: center;
    gap: 0;
    padding: 10px 12px;
    background-color: #fafafa;
    border-bottom: 1px solid #e0e0e0;
    flex-wrap: wrap;
}

.tool-btn {
    padding: 6px 10px;
    margin: 2px;
    border: 1px solid #e0e0e0;
    background-color: white;
    border-radius: 3px;
    cursor: pointer;
    font-size: 13px;
    color: #666;
    font-weight: 500;
    transition: all 0.2s ease;

    &:hover {
        background-color: #f5f5f5;
        border-color: var(--primaryColor);
        color: var(--primaryColor);
    }

    &.active {
        background-color: var(--primaryColor);
        color: white;
        border-color: var(--primaryColor);
    }
}

.divider {
    width: 1px;
    height: 24px;
    background-color: #e0e0e0;
    margin: 0 6px;
}

.editor-content {
    min-height: 280px;
    padding: 14px;
    overflow-y: auto;
    outline: none;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
    font-size: 14px;
    line-height: 1.7;
    color: #333;
    background-color: white;

    &:focus {
        outline: none;
        background-color: #fafafa;
    }

    img {
        max-width: 100%;
        height: auto;
        margin: 12px 0;
        border-radius: 4px;
        display: block;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    }

    video {
        max-width: 100%;
        height: auto;
        margin: 12px 0;
        border-radius: 4px;
        display: block;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    }

    p {
        margin: 0.8em 0;
    }

    ul {
        margin: 0.8em 0 0.8em 2em;
        padding: 0;
        list-style-type: disc;
    }

    ol {
        margin: 0.8em 0 0.8em 2em;
        padding: 0;
        list-style-type: decimal;
    }

    li {
        margin: 0.4em 0;
    }

    a {
        color: var(--primaryColor);
        text-decoration: underline;
        cursor: pointer;

        &:hover {
            opacity: 0.8;
        }
    }

    blockquote {
        border-left: 3px solid var(--primaryColor);
        padding-left: 12px;
        margin: 12px 0;
        color: #666;
        font-style: italic;
    }

    code {
        background-color: #f5f5f5;
        padding: 2px 6px;
        border-radius: 3px;
        font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', monospace;
        font-size: 13px;
    }

    pre {
        background-color: #f5f5f5;
        padding: 12px;
        border-radius: 4px;
        overflow-x: auto;
        margin: 12px 0;

        code {
            background-color: transparent;
            padding: 0;
        }
    }
}
</style>
