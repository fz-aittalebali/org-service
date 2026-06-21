package org.dxc.orgservice.shared.config;

import org.dxc.orgservice.shared.application.ports.in.IBulkCommandHandler;
import org.dxc.orgservice.shared.application.ports.in.IEventDrivenHandler;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.dxc.orgservice.shared.application.ports.in.IReturnCommandHandler;
import org.dxc.orgservice.shared.application.ports.in.IVoidCommandHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "org.dxc.orgservice",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        IVoidCommandHandler.class,
                        IReturnCommandHandler.class,
                        IQueryHandler.class,
                        IBulkCommandHandler.class,
                        IEventDrivenHandler.class
                }
        )
)
public class ApplicationLayerScannerConfig {
}
