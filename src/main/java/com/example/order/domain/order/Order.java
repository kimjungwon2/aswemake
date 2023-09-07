package com.example.order.domain.order;

import com.example.order.common.domain.BaseEntity;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.delivery.Delivery;
import com.example.order.domain.member.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProduct = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    @NotNull
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder
    public Order(
            Long id,
            Member member,
            List<OrderProduct> orderProduct,
            Delivery delivery,
            Integer totalPrice,
            OrderStatus status
    ) {
        this.id = id;
        this.member = member;
        this.orderProduct = orderProduct;
        this.delivery = delivery;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void addOrderProduct(OrderProduct orderProduct){
        this.orderProduct.add(orderProduct);
        orderProduct.setOrder(this);
    }
}
