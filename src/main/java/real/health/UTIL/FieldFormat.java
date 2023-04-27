package real.health.UTIL;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

public class FieldFormat {
    public static JFormattedTextField createPhoneNumber(boolean phone) {
        MaskFormatter maskFormatter = null;
        try {
            if (phone) {
                maskFormatter = new MaskFormatter("###-###-####");
            } else {
                maskFormatter = new MaskFormatter("##/##/####");
            }
            
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        if (maskFormatter != null && phone) {
            maskFormatter.setPlaceholderCharacter('_');
        }
        

        return new JFormattedTextField(maskFormatter);
    }

    
}
