package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.*;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * a HYG data base loader
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {

    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        try (BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            //line indicating the column labels
            String lineString = input.readLine();
            lineString = input.readLine();

            while (lineString != null) {
                String[] stringTable = lineString.split(",");

                String hipparcosFromTable = stringTable[HYGCatCol.HIP.ordinal()];
                int hipparcosNumber = (hipparcosFromTable.isBlank()) ? 0 : (Integer.parseInt(hipparcosFromTable));

                String nameProperFromTable = stringTable[HYGCatCol.PROPER.ordinal()];
                String nameBayerFromTable = stringTable[HYGCatCol.BAYER.ordinal()];
                String name = (nameProperFromTable.isBlank()) ? (((nameBayerFromTable.isBlank()) ? "?" :
                        nameBayerFromTable) + " " + stringTable[HYGCatCol.CON.ordinal()]) : nameProperFromTable;

                EquatorialCoordinates equatorialCoordinates = EquatorialCoordinates.of(
                        Double.parseDouble(stringTable[HYGCatCol.RARAD.ordinal()]),
                        Double.parseDouble(stringTable[HYGCatCol.DECRAD.ordinal()]));

                String magnitudeFromTable = stringTable[HYGCatCol.MAG.ordinal()];
                double magnitude = (magnitudeFromTable.isBlank()) ? 0 : Double.parseDouble(magnitudeFromTable);

                String colorIndexFromTable = stringTable[HYGCatCol.CI.ordinal()];
                double colorIndex = (colorIndexFromTable.isBlank()) ? 0 : Double.parseDouble(colorIndexFromTable);
                builder.addStar(
                        new Star(hipparcosNumber, name, equatorialCoordinates, (float) magnitude, (float) colorIndex));

                lineString = input.readLine();
            }
        }
    }

    /**
     * An enumeration containing the ordered column labels of the HYG catalogue
     */
    private enum HYGCatCol {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC, RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON, COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }
}
