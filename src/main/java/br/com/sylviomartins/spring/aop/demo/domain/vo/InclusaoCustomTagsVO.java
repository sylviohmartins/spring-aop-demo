package br.com.sylviomartins.spring.aop.demo.domain.vo;


import br.com.sylviomartins.spring.aop.demo.annotation.nested.TagField;
import br.com.sylviomartins.spring.aop.demo.domain.document.Boleto;
import br.com.sylviomartins.spring.aop.demo.util.CustomMethodUtils;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InclusaoCustomTagsVO {

    @TagField(clazz = Boleto.class, fields = {"status"}, key = "tipo")
    private String tipo;

    @TagField(clazz = Boleto.class, fields = {"channelSource"}, key = "canal")
    private String canal;

    @TagField(clazz = Boleto.class, fields = {"paymentAmount", "maximumPaymentAmount"}, key = "pagamento", expressionClass = CustomMethodUtils.class, expressionMethod = "isPaymentFull")
    private String pagamento;

    @TagField(clazz = Boleto.class, fields = {"method"}, key = "banco")
    private String banco;

    @TagField(clazz = Boleto.class, fields = {"date"}, key = "operacao", expressionClass = CustomMethodUtils.class, expressionMethod = "isPaymentToday")
    private String operacao;

    @TagField(clazz = Boleto.class, fields = {"id"}, key = "contingencia", expressionClass = CustomMethodUtils.class, expressionMethod = "isPaymentToday")
    private String contingencia;

}
