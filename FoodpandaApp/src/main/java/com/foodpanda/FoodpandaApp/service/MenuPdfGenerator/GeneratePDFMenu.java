package com.foodpanda.FoodpandaApp.service.MenuPdfGenerator;

import com.foodpanda.FoodpandaApp.model.Menu;
import com.foodpanda.FoodpandaApp.model.MenuItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;

public class GeneratePDFMenu {
    private static final Logger logger = LoggerFactory.
            getLogger(GeneratePDFMenu.class);

    public ByteArrayInputStream generateMenuPdf(Set<MenuItem> menuItems) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            PdfWriter.getInstance(document, out);
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Paragraph intro = new Paragraph("Restaurant menu: \n", font);
            document.add(intro);
            for (MenuItem menuItem : menuItems) {
                Paragraph menuItemPar = new Paragraph(menuItem.getName() + " - " + menuItem.getPrice() + " $\n", font);
                document.add(menuItemPar);
            }
            document.close();
        }catch (DocumentException e) {
            logger.error(e.toString());
        }

        return new ByteArrayInputStream(out.toByteArray());

    }
}
