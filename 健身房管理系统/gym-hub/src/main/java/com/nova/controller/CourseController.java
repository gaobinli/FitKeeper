package com.nova.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.nova.domain.dto.req.CourseReqDto;
import com.nova.domain.dto.res.CourseResDto;
import com.nova.service.CourseService;
import com.baomidou.mybatisplus.ext.Pager;
import com.nova.domain.ResultDTO;
import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;
import java.util.List;

@Data
@RestController
@Tag(name = "course")
@RequestMapping("/course")
@SuppressWarnings("unchecked")
public class CourseController {

    private final CourseService courseService;

    /**
     * 创建一个资源对象
     *
     * @return 返回新创建的资源对象
     */
    @ApiOperation(value = "创建一个资源对象（返回新创建的资源对象）", tags = {"course"}, nickname = "addCourse")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "000000:成功，否则失败")})
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public ResultDTO<CourseResDto> addCourse(
        @ApiParam(value = "要保存的对象", required = true) @Valid @RequestBody CourseReqDto reqDto) {
        CourseResDto resDto = courseService.save(reqDto);
        return ResultDTO.ok(resDto);
    }

    /**
     * 查询所有数据
     *
     * @return 返回新创建的资源对象
     */
    @ApiOperation(value = "查询所有数据", tags = {"course"}, nickname = "getList")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作是否成功,000000:成功，否则失败")})
    @GetMapping(value = "/list", produces = {"application/json"})
    public ResultDTO<List<CourseResDto>> getList(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<CourseResDto> resDtos = courseService.getList(params);
        return ResultDTO.ok(resDtos);
    }

    /**
     * 删除单个或一系列资源对象
     *
     * @param ids 用英文逗号隔开
     * @return 返回一个空文档
     */
    @ApiOperation(value = "删除单个或一系列资源对象", tags = {"course",}, nickname = "deleteCourse")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作是否成功,000000:成功，否则失败")})
    @DeleteMapping(value = "/{ids}", produces = {"application/json"})
    public ResultDTO<Void> deleteCourse(
        @ApiParam(value = "要删除的对象主键，多个对象主键可以用英文逗号隔开", required = true) @PathVariable("ids") String ids) {
        boolean bl = courseService.deleteByIds(ids);
        return ResultDTO.ok();
    }

    /**
     * 查询一系列资源对象，也就是列表
     *
     * @param params 参数params
     * @return 返回一系列资源对象
     */
    @ApiOperation(value = "查询一系列资源对象，也就是列表(qp-参数支持的操作符号有: eq(=),ne(!=),gt(>),lt(<),ge(>=),le(<=),in,like,notLike,likeleft(左边LIKE '%xxx'),likeright(右边LIKE 'xx%'))", tags = {
            "course",}, nickname = "getCourseList")
    @ApiImplicitParams({@ApiImplicitParam(name = "currentPage", value = "当前页数", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "sorter", value = "排序条件 desc-字段名或者asc-字段名", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-name-eq", value = "课程名称", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-coachId-eq", value = "教练", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-price-eq", value = "价格", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-classificationId-eq", value = "分类", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-coverUrl-eq", value = "课程封面", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-content-eq", value = "课程内容", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-courseTime-eq", value = "课程时间", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-limitNumber-eq", value = "限制人数", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-auditDesc-eq", value = "审核描述", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-auditStatus-eq", value = "审核状态：1 待审核、2 审核驳回、3 审核通过", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-recommendedScore-eq", value = "推荐指数", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-createUser-eq", value = "创建人", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-createTime-eq", value = "创建时间", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-modifyUser-eq", value = "修改人", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "qp-modifyTime-eq", value = "修改时间", paramType = "query", dataTypeClass = Map.class),

    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作是否成功,000000:成功，否则失败")})
    @GetMapping(produces = {"application/json"})
    public ResultDTO<Pager<CourseResDto>> getCourseList(@ApiIgnore @RequestParam Map<String, Object> params) {
        Pager<CourseResDto> resDtoPager = courseService.doPager(params);
        return ResultDTO.ok(resDtoPager);
    }

    /**
     * 查询一个资源对象
     *
     * @return 返回单独的资源对象
     */
    @ApiOperation(value = "查询一个资源对象", tags = {"course"}, nickname = "getOneCourse")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作是否成功,000000:成功，否则失败")})
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResultDTO<CourseResDto> getOneCourse(@ApiParam(value = "要查询的对象主键", required = true) @PathVariable("id") Long id) {
        CourseResDto resDto = courseService.selectOne(id);
        return ResultDTO.ok(resDto);
    }

    /**
     * 查询一个资源对象
     *
     * @param params 能确定唯一对象的条件
     * @return 返回单独的资源对象
     */
    @ApiOperation(value = "查询一个资源对象(若不唯一则随机返回符合条件的一个)", tags = {"course"}, nickname = "getOneCourseByMap")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作是否成功,000000:成功，否则失败")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "课程名称", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "coachId", value = "教练", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "price", value = "价格", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "classificationId", value = "分类", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "coverUrl", value = "课程封面", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "content", value = "课程内容", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "courseTime", value = "课程时间", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "limitNumber", value = "限制人数", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "auditDesc", value = "审核描述", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态：1 待审核、2 审核驳回、3 审核通过", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "recommendedScore", value = "推荐指数", paramType = "query", dataTypeClass = Map.class),

    })
    @GetMapping(value = "/one", produces = {"application/json"})
    public ResultDTO<CourseResDto> getOneCourse(@ApiIgnore @RequestParam Map<String, Object> params) {
        CourseResDto resDto = courseService.selectOne(params);
        return ResultDTO.ok(resDto);
    }

    /**
     * 更新一个资源对象(部分更新)
     *
     * @return 返回完整的资源对象
     */
    @ApiOperation(value = "更新一个资源对象(更新参数的全部属性)", tags = {"course"}, nickname = "updateCourseByMap")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "课程名称", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "coachId", value = "教练", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "price", value = "价格", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "classificationId", value = "分类", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "coverUrl", value = "课程封面", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "content", value = "课程内容", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "courseTime", value = "课程时间", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "limitNumber", value = "限制人数", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "auditDesc", value = "审核描述", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态：1 待审核、2 审核驳回、3 审核通过", paramType = "query", dataTypeClass = Map.class),
            @ApiImplicitParam(name = "recommendedScore", value = "推荐指数", paramType = "query", dataTypeClass = Map.class),
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作是否成功,000000:成功，否则失败")})
    @PatchMapping(value = "/map/{id}", produces = {"application/json"})
    public ResultDTO<CourseResDto> updateCourse(@ApiParam(value = "对象ID", required = true) @PathVariable("id") Long id,
    @ApiIgnore @RequestParam Map<String, Object> params) {
        courseService.updateProps(id, params);
        CourseResDto resDto = courseService.selectOne(id);
        return ResultDTO.ok(resDto);
    }

    /**
     * 更新一个资源对象(部分更新)
     *
     * @return 返回完整的资源对象
     */
    @ApiOperation(value = "更新一个资源对象(部分更新,更新对象里有值的属性,空值不更新)", tags = {"course"}, nickname = "patchUpdateCourse")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作是否成功,000000:成功，否则失败")})
    @PatchMapping(value = "/{id}", produces = {"application/json"})
    public ResultDTO<CourseResDto> patchUpdateCourse(@ApiParam(value = "对象ID", required = true) @PathVariable("id") Long id,
        @ApiParam(value = "要修改的对象，对象属性有值的才更新,即null值不更新", required = true) @Valid @RequestBody CourseReqDto reqDto) {
        courseService.updateProps(id, reqDto);
        CourseResDto resDto = courseService.selectOne(id);
        return ResultDTO.ok(resDto);
    }

    /**
     * 更新一个资源对象（整个对象替换，属性的null值也会更新进去）
     *
     * @return 返回完整的资源对象
     */
    @ApiOperation(value = "更新一个资源对象（更新对象里全部属性，即整个对象替换）", tags = {"course"}, nickname = "putUpdateCourse")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作是否成功,000000:成功，否则失败")})
    @PutMapping(value = "/{id}", produces = {"application/json"})
    public ResultDTO<CourseResDto> putUpdateCourse(@ApiParam(value = "对象ID", required = true) @PathVariable("id") Long id,
        @ApiParam(value = "要修改的对象,对象属性全部更新", required = true) @Valid @RequestBody CourseReqDto reqDto) {
        courseService.updateAllProps(id, reqDto);
        CourseResDto resDto = courseService.selectOne(id);
        return ResultDTO.ok(resDto);
    }
}
