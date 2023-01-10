package org.ncc.github.nqbot.commands;

import net.mamoe.mirai.message.data.*;
import java.util.*;

public class PackagedCommandArg {
    private String commandHead;
    private final List<String> args = new ArrayList<>();
    private final Map<Class<? extends Message>,List<Message>> otherArgs = new HashMap<>();

    public PackagedCommandArg(MessageChain chain){
        for (int i = chain.size() - 1; i >= 0; i--) {
            final Message singleMessage = chain.get(i);
            if (singleMessage instanceof PlainText) {
                final String[][] deserialized = tryDeserializeHead(singleMessage.contentToString());
                if (deserialized != null) {
                    this.commandHead = deserialized[0][0];
                    this.args.addAll(Arrays.asList(deserialized[1]));
                }else {
                    this.args.addAll(Arrays.asList(singleMessage.contentToString().split(" ")));
                }
                continue;
            }
            Class<? extends Message> messageClass = singleMessage.getClass();
            if (!this.otherArgs.containsKey(messageClass)) {
                this.otherArgs.put(messageClass, new ArrayList<>());
            }
            this.otherArgs.get(messageClass).add(singleMessage);
        }
        if (this.commandHead == null) {
            this.commandHead = "nope";
        }
    }

    public String getCommandHead(){
        return this.commandHead;
    }

    public List<String> getArgs() {
        return this.args;
    }

    public Map<Class<? extends Message>, List<Message>> getOtherArgs() {
        return this.otherArgs;
    }

    private static String[][] tryDeserializeHead(String input){
        final String[] fixed = input.split(" ");
        if (fixed.length>=1){
            final String commandHead = fixed[0];
            final String[] args = new String[fixed.length-1];
            System.arraycopy(fixed, 1, args, 0, fixed.length - 1);
            if (commandHead.startsWith("#") && commandHead.length() > 1){
                final String[][] result = new String[2][Math.max(args.length, 1)];
                result[0][0] = commandHead;
                System.arraycopy(args, 0, result[1], 0, args.length);
                return result;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return String.format("@PackagedCommandArg[head=%s,str_arg=%s,other_args=%s]",this.commandHead,this.args,this.otherArgs);
    }
}
