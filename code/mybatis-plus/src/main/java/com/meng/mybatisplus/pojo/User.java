package com.meng.mybatisplus.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //对应数据库中的主键（uuid,自增id，【雪花算法】，redis，zookeeper）
    //MP中的注解
//    @TableId(type = IdType.ID_WORKER)//默认的全局唯一
    @TableId(type = IdType.AUTO)//自增的，注意：要求数据库中的主键一定要是自增的！！
    private Long id;
    private String name;
    private Integer age;
    private String email;
    @Version //乐观锁的version注解(防止数据多线程修改引发的错误）
    private Integer version;

    @TableLogic//逻辑删除
    private Integer deleted;

    //字段添加填充内容
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;//更新时间

}
