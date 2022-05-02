package com.example.MyNetWork.controller;


import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.DetailsService;
import com.example.MyNetWork.service.KafkaMessageSender;
import com.example.MyNetWork.service.UserService;
import com.example.detailsapi.model.Details;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class DetailsController {

    @Autowired
    private UserService userService;
    @Autowired
    private KafkaMessageSender kafkaMessageSender;
    @Autowired
    private DetailsService detailsService;




    @SneakyThrows
    @GetMapping("/change")
    public String showChange( Model model, Model modelName) {
        User user = userService.findUserByUsername(userService.getCurrentUsername());

        kafkaMessageSender.send(user.getId());
        Details details= detailsService.getDetails(user.getId());
        if(details == null){
            Details  usDetails = new Details();
            usDetails.setId(user.getId());
        }
        model.addAttribute("UserDetails",details);
        modelName.addAttribute("Name", userService.getCurrentUsername());

        return "UserChangeInfo";
    }

    @PostMapping("/change")
    public String changeUser(
            @ModelAttribute("UserDetails") Details details,
            BindingResult bindingResult) {



        if(bindingResult.hasErrors()) {
            return "UserChangeInfo";
        }
        User user = userService.findUserByUsername(userService.getCurrentUsername());

        details.setId(user.getId());
        kafkaMessageSender.send(details);


        return "redirect:/page/"+user.getUsername();
    }


}