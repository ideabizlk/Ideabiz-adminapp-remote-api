package lk.dialog.ideabiz.adminapp.app.Controllers;

import com.google.gson.Gson;
import lk.dialog.ideabiz.adminApp.commonModels.*;
import lk.dialog.ideabiz.adminApp.commonModels.Enumarator.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class SubscriberController {

    public static Gson gson = null;
    final static Logger logger = Logger.getLogger(SubscriberController.class);

    public SubscriberController() {
        gson = new Gson();
    }


    @RequestMapping(value = "/{msisdn}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String SubscriberInfo(@PathVariable("msisdn") String msisdn, HttpServletRequest request) {
        try {
            String IP = request.getRemoteAddr();
            logger.info("Subscriber info :" + msisdn + " - " + IP);
            Util.validateIP(IP);

            msisdn = Util.validateMSISDN(msisdn);

            /*
            call DB and get Information;
            */

            /*
            sample code
             */
            Subscription subscription = new Subscription();
            subscription.setNumber(msisdn);
            subscription.setStatus(SUBSCRIPTION_STATUS.SUBSCRIBED);
            subscription.setRegistration(new RegistrationInfo("2015-01-01 06:00:00", EVENT_METHOD.SMS));
            subscription.setUnregistration(new RegistrationInfo("2015-01-01 06:00:00", EVENT_METHOD.SMS));
            /*
            end sample code
             */

            return gson.toJson(new SubscriptionResult(subscription));
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            ServerError er = new ServerError("ERROR", e.getMessage());
            return gson.toJson(new ErrorResult(er));
        }
    }

    @RequestMapping(value = "/{msisdn}/history/{offset}/{limit}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String SubscriberInfo(@PathVariable("msisdn") String msisdn, @PathVariable("offset") String offset, @PathVariable("limit") String limit, HttpServletRequest request) {
        try {
            String IP = request.getRemoteAddr();
            logger.info("History info :" + msisdn + " - [" + offset + "|" + limit + "] - " + IP);
            Util.validateIP(IP);

            int offsetInt = Integer.parseInt(offset);
            int limitInt = Integer.parseInt(limit);

            msisdn = Util.validateMSISDN(msisdn);

            /*
            call DB and get Information;
             */

            /*
            sample
             */
            SubscriberHistory history = new SubscriberHistory();
            history.setNumber(msisdn);
            history.setLimit(limitInt);
            history.setOffset(offsetInt);

            history.getHistory().add(new History("2015-01-01 00:00:00", EVENT_TRIGGER.SUBSCRIBER, EVENT_TYPE.SUBSCRIBE, "", EVENT_STATUS.SUCCESS));
            history.getHistory().add(new History("2015-01-01 00:00:00", EVENT_TRIGGER.SYSTEM, EVENT_TYPE.UNSUBSCRIBE, "", EVENT_STATUS.SUCCESS));
            history.getHistory().add(new History("2015-01-01 00:00:00", EVENT_TRIGGER.ADMIN, EVENT_TYPE.SMS, "", EVENT_STATUS.SUCCESS));
            /*
            End sample
             */

            return gson.toJson(new SubscriberHistoryResult(history));
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            ServerError er = new ServerError("ERROR", e.getMessage());
            return gson.toJson(new ErrorResult(er));
        }
    }

    @RequestMapping(value = "/{msisdn}/unsubscribe", method =  { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String RemoveSubscription(@PathVariable("msisdn") String msisdn,  HttpServletRequest request) {
        try {
            String IP = request.getRemoteAddr();
            logger.info("Unsubscribe info :" + msisdn + " - " + IP);
            Util.validateIP(IP);

            msisdn = Util.validateMSISDN(msisdn);


            /*
            call DB and get Information;
             */

            /*
            sample
             */
            Subscription subscription = new Subscription();
            subscription.setNumber(msisdn);
            subscription.setStatus(SUBSCRIPTION_STATUS.UNSUBSCRIBED);
            /*
            End sample
             */

            return gson.toJson(new SubscriptionResult(subscription));
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            ServerError er = new ServerError("ERROR", e.getMessage());
            return gson.toJson(new ErrorResult(er));
        }
    }

    @RequestMapping(value = "/*", method =  { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE }, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String Default(  HttpServletRequest request) {
        try {
            String IP = request.getRemoteAddr();
            logger.info("Default info :" + IP);
            Util.validateIP(IP);


            ServerError er = new ServerError("ERROR", "Wrong method");
            return gson.toJson(new ErrorResult(er));

        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            ServerError er = new ServerError("ERROR", e.getMessage());
            return gson.toJson(new ErrorResult(er));
        }
    }
}