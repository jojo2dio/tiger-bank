package org.zoo.loan.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.zoo.loan.dal.Loan;
import org.zoo.loan.facade.LoanGrantService;
import org.zoo.loan.facade.LoanService;
import org.zoo.loan.model.LoanDTO;
import org.zoo.loan.model.LoanGrantDTO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 贷款自动放款定时任务（XXL-Job 2.2.0+版本）
 * 每天11点执行，扫描待审批贷款并创建放款记录
 */
@Component
public class LoanAutoGrantJob {  // 不再需要继承IJobHandler

    private static final Logger logger = LoggerFactory.getLogger(LoanAutoGrantJob.class);

    // 系统管理员ID（建议从配置中心获取）
    private static final Long SYSTEM_OPERATOR_ID = 1L;

    @Resource
    private LoanService loanService;

    @Resource
    private LoanGrantService loanGrantService;

    /**
     * 任务执行方法（使用@XxlJob注解标记）
     * value值对应XXL-Job Admin中配置的JobHandler名称
     */
    @XxlJob("loanAutoGrantJob")
    public ReturnT<String> execute(String param) throws Exception {
        logger.info("===== 贷款自动放款任务开始执行 =====");
        logger.info("任务参数：{}", param);

        try {
            // 1. 查询所有待审批的贷款（status=3）
            List<Loan> pendingLoans = loanService.selectByStatus(5);
            logger.info("查询到待审批贷款数量：{}", pendingLoans.size());

            if (pendingLoans.isEmpty()) {
                logger.info("没有待审批的贷款，任务结束");
                return ReturnT.SUCCESS;
            }

            // 2. 遍历处理每笔贷款
            int successCount = 0;
            int failCount = 0;

            for (Loan loan : pendingLoans) {
                try {
                    // 3. 构建放款DTO
                    LoanGrantDTO grantDTO = new LoanGrantDTO();
                    grantDTO.setLoanId(loan.getId());
                    grantDTO.setGrantAmount(loan.getAmount()); // 放款金额=贷款总金额
                    grantDTO.setRemark("系统自动放款（定时任务）");

                    // 4. 调用创建放款记录接口
                    loanGrantService.createGrantRecord(grantDTO, SYSTEM_OPERATOR_ID);
                    loan.setStatus(1);
                    LoanDTO loanDTO = new LoanDTO();
                    BeanUtils.copyProperties(loan, loanDTO);
                    loanService.update(loanDTO);
                    successCount++;
                    logger.info("贷款ID: {} 自动放款成功", loan.getId());
                } catch (Exception e) {
                    failCount++;
                    logger.error("贷款ID: {} 自动放款失败", loan.getId(), e);
                    continue; // 单个失败不影响整体
                }
            }

            // 5. 输出执行结果
            logger.info("任务执行完成，成功：{} 笔，失败：{} 笔", successCount, failCount);

        } catch (Exception e) {
            logger.error("任务执行异常", e);
            return new ReturnT<>(ReturnT.FAIL_CODE, "任务执行失败：" + e.getMessage());
        }

        return ReturnT.SUCCESS;
    }
}
