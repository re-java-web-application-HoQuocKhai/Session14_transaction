package com.re.transaction.controller;

import com.re.transaction.service.impl.BankingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {
    private final BankingServiceImpl bankingService;

    @GetMapping("/accounts")
    public String getAllAccount(Model model) {
        model.addAttribute("accounts", bankingService.getAccounts());
        return "account/list-account";
    }

    @GetMapping("/logs")
    public String getAllLogs(Model model) {
        model.addAttribute("logs", bankingService.getTransactions());
        return "transaction/logs";
    }

    @GetMapping("/transfer")
    public String getTransfer() {
        return "banking/form-transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId, 
                           @RequestParam Long toId, 
                           @RequestParam double amount,
                           RedirectAttributes redirectAttributes) {
        try {
            bankingService.transferMoney(fromId, toId, amount);
            redirectAttributes.addFlashAttribute("message", "Chuyển tiền thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Chuyển tiền thất bại: " + e.getMessage());
        }
        return "redirect:/bank/accounts";
    }

}
