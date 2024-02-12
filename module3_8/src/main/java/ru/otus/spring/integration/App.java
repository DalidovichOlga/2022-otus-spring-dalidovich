package ru.otus.spring.integration;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.otus.spring.integration.Service.CorrelationIngredientsStrategy;
import ru.otus.spring.integration.domain.Ingredients;
import ru.otus.spring.integration.domain.LayerCake;
import ru.otus.spring.integration.domain.OrderItem;

import java.util.concurrent.ForkJoinPool;


@IntegrationComponentScan
@SuppressWarnings({"resource", "Duplicates", "InfiniteLoopStatement"})
@ComponentScan
@Configuration
@EnableIntegration
public class App {
    static Long currentId = 0L;

    private static final String[] TORT_LIST = {"Ореховый", "Наполеон", "Птичье молоко", "Панчо", "Прага"};
    private static final String[] CONGRATULATION_LIST = {"С днем родждения", "С новым годом", ""};

    @Bean
    public QueueChannel orderChannel() {
        return MessageChannels.queue(50).get();
    }

    @Bean
    public PublishSubscribeChannel buildCake() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel tortChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get();
    }

    @Bean
    public IntegrationFlow stepOneFlow() {
        //испечь коржи и сделать крем параллельно

        ForkJoinPool pool = ForkJoinPool.commonPool();
        return IntegrationFlows.from("orderChannel")
                .publishSubscribeChannel(pool, subscription -> subscription.
                        subscribe(
                                subflow -> subflow.handle("creamMaker", "make").channel("buildCake"))
                        .subscribe(
                                subflow -> subflow.handle("korzhiMaker", "make").channel("buildCake"))
                ).get();
    }

    @Bean
    public IntegrationFlow stepToFlow() {
        //Собрать крем и коржи в гововый торт
        return IntegrationFlows.from("buildCake")
                // собрать пары крем+коржи
                .aggregate(a -> a.correlationStrategy(new CorrelationIngredientsStrategy())
                        .releaseStrategy(group -> group.size() == 2))
                // сделать торт
                .handle("cakeСollectorService", "cook")
                // подписать торт , если нужно
                .<LayerCake, Boolean>route(LayerCake::needText, mapping -> mapping
                        .subFlowMapping(true, sf -> sf.handle("сongratulatorService", "makeText")
                                .channel("tortChannel")
                        )
                        .subFlowMapping(false, sf -> sf
                                .channel("tortChannel")
                        )
                )
                .get();

    }

    private static void PrintTortInfo(Message<?> message) {
        LayerCake cake = (LayerCake) message.getPayload();
        System.out.println("Испекли торт:");
        System.out.println("Заказ: " + cake.getOrder().getId() + ". Торт: " + cake.getOrder().getItemName());
        System.out.println("Состав: " + cake.getKorzi() + ", " + cake.getCream());
        if (cake.needText())
            System.out.println("Подписан: " + cake.getText());

    }

    public static void main(String[] args) throws Exception {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);


        CakeOrderProcessor сakeOrderProcessor = ctx.getBean(CakeOrderProcessor.class);

        SubscribableChannel channel2 = ctx.getBean("tortChannel", SubscribableChannel.class);

        ForkJoinPool pool = ForkJoinPool.commonPool();

        //получаем готовые торты
        channel2.subscribe(App::PrintTortInfo);

        while (true) {
            Thread.sleep(10000);

            pool.execute(() -> {
                OrderItem item = generateOrderItem();
                System.out.println("Новый заказ: " + item.getId() + ". Печем торт:" + item.getItemName());
                сakeOrderProcessor.process(item);

            });
        }
    }

    private static OrderItem generateOrderItem() {
        currentId++;
        return new OrderItem(currentId,
                TORT_LIST[RandomUtils.nextInt(0, TORT_LIST.length)],
                CONGRATULATION_LIST[RandomUtils.nextInt(0, CONGRATULATION_LIST.length)]
        );
    }
}
