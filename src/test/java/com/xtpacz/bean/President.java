package com.xtpacz.bean;

/**
 * @ClassName President
 * @Description TODO
 * @Author Administrator
 * @Date 2023/2/15
 * @Version 1.0
 **/
public class President {
    private Hand hand;
    private Leg leg;
    
    
    public void doIt(){
        hand.shakeHand();
        leg.run();
    }
}
