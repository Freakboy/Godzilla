package vip.youwe.shell.utils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class automaticBindClick {

    public static void bindButtonClick(final Object fieldClass, Object eventClass) {
        try {
            Field[] fields = fieldClass.getClass().getDeclaredFields();
            for (int i = 0;i < fields.length; i++) {
                Field field = fields[i];
                if (field.getType().isAssignableFrom(Button.class)) {
                    field.setAccessible(true);
                    Button fieldValue = (Button) field.get(fieldClass);
                    String fieldName = field.getName();
                    if (fieldValue != null) {
                        try {
                            if (fieldName.equals("selectdFileButton")) {
                                System.out.println();
                            }
                            final Method method = eventClass.getClass().getDeclaredMethod(fieldName + "Click", ActionEvent.class);
                            method.setAccessible(true);
                            if (method != null) {
                                fieldValue.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            method.invoke(fieldClass, e);
                                        } catch (Exception e1) {
                                            Log.error(e1);
                                        }
                                    }
                                });
                            }
                        } catch (NoSuchMethodException e) {
                            System.out.println(fieldName + "Click" + "  未实现");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindJButtonClick(final Object fieldClass, Object eventClass) {
        try {
            Field[] fields = fieldClass.getClass().getDeclaredFields();
            for (int i = 0;i < fields.length; i++) {
                Field field = fields[i];
                if (field.getType().isAssignableFrom(JButton.class)) {
                    field.setAccessible(true);
                    JButton fieldValue = (JButton) field.get(fieldClass);
                    String fieldName = field.getName();
                    if (fieldValue != null) {
                        try {
                            final Method method = eventClass.getClass().getDeclaredMethod(fieldName + "Click", ActionEvent.class);
                            method.setAccessible(true);
                            if (method != null) {
                                fieldValue.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            method.invoke(fieldClass, e);
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } catch (NoSuchMethodException e) {
                            Log.error(fieldName + "Click" + "  未实现");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindMenuItemClick(Object item, Map<String, Method> methodMap, Object eventClass) {
        MenuElement[] menuElements = ((MenuElement) item).getSubElements();
        if (methodMap == null) {
            methodMap = getMenuItemMethod(eventClass);
        }
        if (menuElements.length == 0) {
            if (item.getClass().isAssignableFrom(JMenuItem.class)) {
                Method method = methodMap.get(((JMenuItem) item).getActionCommand() + "MenuItemClick");
                addMenuItemClickEvent(item, method, eventClass);
            }

        } else {
            for (int i = 0; i < menuElements.length; i++) {
                MenuElement menuElement = menuElements[i];
                Class<?> itemClass = menuElement.getClass();
                if (itemClass.isAssignableFrom(JPopupMenu.class)
                        || itemClass.isAssignableFrom(JMenu.class)) {
                    bindMenuItemClick(menuElement, methodMap, eventClass);
                } else if (item.getClass().isAssignableFrom(JMenuItem.class)) {
                    Method method = methodMap.get(((JMenuItem) menuElement).getActionCommand() + "MenuItemClick");
                    addMenuItemClickEvent(menuElement, method, eventClass);
                }
            }
        }
    }

    private static Map<String, Method> getMenuItemMethod(Object eventClass) {
        Method[] methods = eventClass.getClass().getDeclaredMethods();

        Map<String, Method> methodMap = new HashMap<>();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1
                    && parameterTypes[0].isAssignableFrom(ActionEvent.class)
                    && method.getReturnType().isAssignableFrom(Void.TYPE)
                    && method.getName().endsWith("MenuItemClick")) {
                methodMap.put(method.getName(), method);
            }
        }
        return methodMap;
    }

    private static void addMenuItemClickEvent(Object item, final Method method, final Object eventClass) {
        if (method != null && eventClass != null && item.getClass().isAssignableFrom(JMenuItem.class)) {
            ((JMenuItem) item).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    try {
                        method.setAccessible(true);
                        method.invoke(eventClass, paramActionEvent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
