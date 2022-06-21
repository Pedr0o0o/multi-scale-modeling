package company.com;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GrainGrowth
{
    Particle[][] tab;
    int sizeX = 1;
    int sizeY = 1;
    int grainAmount = 1;

    public GrainGrowth(int grainAmount)
    {
        this.grainAmount = grainAmount;
    }

    public void setSizeX(int sizeX)
    {
        this.sizeX = sizeX;
    }
    public void setSizeY(int sizeY)
    {
        this.sizeY = sizeY;
    }

    void initializeArea() {
        Random rand = new Random();
        tab = new Particle[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++)
        {
            for (int j = 0; j < sizeY; j++)
            {
                tab[i][j] = new Particle(0, rand.nextInt(4)); //ustawianie en pocżątkowej w losowych miejscach
            }
        }


    }


    //CA
    void grainGrowthHex(boolean periodic) throws IOException {

        if(periodic){
            periodicCondition();
        }

        //warunek stopu - licznik pustych komórek
        int emptyCell = 1;
        //liczba iteracji
        int h = 0;


        //pętla while - wykonuje się dopóki, dopóty na planszy są puste komórki
        while(emptyCell > 0)
        {

            if(periodic){
                periodicCondition();
            }

            Particle[][] tab2 = new Particle[sizeX][sizeY];

            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < sizeY; j++)
                {
                    tab2[i][j] = new Particle(0,0);
                }
            }

            //licznik pustych komórek ustawiony na zero
            emptyCell = 0;

            //przechodzimy całą planszę, tablica2 będzie przyjmowała wartości przypisane na bazie stanu tablicy poprzedzającej
            for (int i = 1; i < sizeX - 1; i++)
            {
                for (int j = 1; j < sizeY - 1; j++)
                {
                    tab2[i][j].id = tab[i][j].id;

                    //jeżeli komórka jest pusta - stosujemy warunek na bazie sąsiedztwa hexagonalnego
                    if (tab[i][j].id == 0)
                    {
                        int restultHex = hex(i, j);
                        tab2[i][j].id = restultHex;
                    }

                    //zliczamy puste komórki
                    if (tab[i][j].id == 0)
                    {
                        emptyCell++;
                    }
                }

            }


            tab = tab2;


            //zwiększamy liczbe iteracji
            h++;

        }

    }
    void grainGrowthMoore(boolean periodic) {

        int emptyCell = 1;
        //liczba iteracji
        int h = 0;

        //pętla while - wykonuje się dopóki, dopóty na planszy są puste komórki
        while(emptyCell > 0)
        {
            if(periodic){
                periodicCondition();
            }

            Particle[][] tab2 = new Particle[sizeX][sizeY];

            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < sizeY; j++)
                {
                    tab2[i][j] = new Particle(0,0);
                }
            }

            //licznik pustych komórek ustawiony na zero
            emptyCell = 0;

            //przechodzimy całą planszę, tablica2 będzie przyjmowała wartości przypisane na bazie stanu tablicy poprzedzającej
            for (int i = 1; i < sizeX - 1; i++)
            {
                for (int j = 1; j < sizeY - 1; j++)
                {
                    tab2[i][j].id = tab[i][j].id;

                    //jeżeli komórka jest pusta - stosujemy warunek na bazie sąsiedztwa Moora bądź hexagonalnego
                    if (tab[i][j].id == 0)
                    {
                        int resultM = moore(i, j);
                        tab2[i][j].id = resultM;
                    }

                    //zliczamy puste komórki
                    if (tab[i][j].id == 0)
                    {
                        emptyCell++;
                    }
                }

            }
            tab = tab2;

            //zwiększamy liczbe iteracji
            h++;

        }


    }


    //SRX - rekrystalizacja
    void calcEnergy() {
        for(int i = 1; i < sizeX-1; i++)
        {
            for(int j = 1; j < sizeY-1; j++)
            {
                for(int k = -1; k < 2; k++)
                {
                    for(int l = -1; l < 2; l++)
                    {
                        if(tab[i-k][j-l].id != tab[i][j].id)
                        {
                            tab[i-k][j-l].energy ++;
                        }

                    }
                }
            }
        }
    }
    void SRX(boolean mostek) throws IOException {

        calcEnergy();

        ArrayList<XYcontainer> xylist = new ArrayList<>();

        for(int i = 0; i < sizeX; i++)
        {
            for(int j = 0; j < sizeY; j++)
            {
                if(tab[i][j].energy > 3) //pewna ustalona wartość graniczna
                {
                    xylist.add(new XYcontainer(i,j));
                }
            }
        }

        initializeArea(); //czyszczenie przestrzeni

        if(mostek)
        {
            compArea();
        }

        for(int i =0; i <xylist.size(); i++)
        {
            tab[xylist.get(i).x][xylist.get(i).y].id = i+1;
        }
        grainAmount = xylist.size() +1;

        grainGrowthMoore(false);

    }

    //typy sąsiedztwa
    public int hex(int x, int y) {
        //tworzymy tablicę przechowującą ziarna o rozmiarze ilości zarodków
        int [] tabGrain = new int[grainAmount];



        //analizujemy sąsiedztwo każdej komórki (8 komórek wokół)
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                //zbieramy informacje na temat wartości każdej komórki
                if(tab[x+i][y+j].id >= 0)
                {
                    tabGrain[tab[x + i][y + j].id]++;
                }

            }
        }

        if(tab[x-1][y+1].id > 0 && tabGrain[tab[x - 1][y + 1].id] >0)
        {
            tabGrain[tab[x - 1][y + 1].id]--;
        }

        if(tab[x+1][y-1].id > 0 && tabGrain[tab[x + 1][y - 1].id] >0)
        {
            tabGrain[tab[x + 1][y - 1].id]--;
        }


        int max = 0;
        int maxId = 0;


        for(int f = 1; f < tabGrain.length; f++)
        {
            //max pomaga w ustaleniu, którego koloru wokół jest najwięcej; wartości w tablicy - ilości wystąpień id konkretnych ziaren
            if(tabGrain[f] > max)
            {
                max = tabGrain[f];
                maxId = f;
            }
        }

        //max 0 - brak ziaren w otoczeniu
        if(max == 0)
        {
            return 0;
        }

        //przypisujemy komórce takie id(kolor), który wokół niej powtarza się najczęściej
        return maxId;
    }
    public int moore(int x, int y) {

        /*int grain1 = 0;
        int grain2 = 0;*/

        //tworzymy tablicę przechowującą ziarna o rozmiarze ilości zarodków
        int [] tabGrain = new int[grainAmount];

        //analizujemy sąsiedztwo każdej komórki (8 komórek wokół)
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                //zbieramy informacje na temat wartości każdej komórki
                if(tab[x+i][y+j].id >= 0)
                {
                    tabGrain[tab[x + i][y + j].id]++;
                }

            }
        }
        int max = 0;
        int maxId = 0;


        for(int f = 1; f < tabGrain.length; f++)
        {
            //max pomaga w ustaleniu, którego koloru wokół jest najwięcej; wartości w tablicy - ilości wystąpień id konkretnych ziaren
            if(tabGrain[f] > max)
            {
                max = tabGrain[f];
                maxId = f;
            }
        }

        //max 0 - brak ziaren w otoczeniu
        if(max == 0)
        {
            return 0;
        }

        //przypisujemy komórce takie id(kolor), który wokół niej powtarza się najczęściej
        return maxId;
    }

    //motoda monte carlo wraz z incjalizacją początkową
    void nucStartMC() {
        Random rand = new Random();

        for (int i = 0; i < sizeX; i++)
        {
            for (int j = 0; j < sizeY; j++)
            {
                if (tab[i][j].id != -1)
                {
                    tab[i][j].id = rand.nextInt(grainAmount-1)+1; //żeby nigdy nie wylosowało 0
                }

            }
        }
    }
    void monteCarlo(int nSteps, boolean mostek) {
        for(int i = 0; i < nSteps; i++)
        {

            ArrayList<XYcontainer> xylist = new ArrayList<>();

            for (int x = 1; x < sizeX - 1; x++)
            {
                for (int y = 1; y < sizeY - 1; y++)
                {
                    if(tab[x][y].id != -1)
                    {
                        xylist.add(new XYcontainer(x, y));
                    }
                }
            }

            while (xylist.size() > 0) {
                //losowanie
                Random random = new Random();
                int n = random.nextInt(xylist.size());

                System.out.println("mc" + n + "," + xylist.size());

                tab[xylist.get(n).x][xylist.get(n).y].id = hex(xylist.get(n).x, xylist.get(n).y);
                xylist.remove(n);

            }
        }
    }

    //typy zarodkowania
    //zarodkowanie jednorodne // liczba podziałów przestrzeni na x i y
    void homoNuc(int x, int y) {

        int xStep = (sizeX-2)/x;
        int yStep = (sizeY-2)/y;
        System.out.println(xStep+" "+ yStep);
        int grainNumber = 1;

        for(int i = xStep/2; i < sizeX-1; i+= xStep)
        {
            for(int j = yStep/2; j < sizeY-1; j+= yStep)
            {
                if(tab[i][j].id != -1)
                {
                    tab[i][j].id = grainNumber;
                    grainNumber++;

                }

            }
        }

        grainAmount = grainNumber;

    }
    //zarodkowanie losowe
    void randNuc() {
        Random random = new Random();
        int grainNumber = 1;

        while(grainNumber < grainAmount)
        {

            int x = random.nextInt(sizeX - 2) + 1;
            int y = random.nextInt(sizeY - 2) + 1;

            if(tab[x][y].id != -1)
            {
                tab[x][y].id = grainNumber;
                grainNumber++;
            }

        }

    }

    //periodyczne warunki brzegowe
    void periodicCondition()
    {
        for(int y = 1; y < sizeY - 1; y++)
        {
            tab[sizeX-1][y].id = tab[1][y].id;
            tab[0][y].id = tab[sizeX-2][y].id;
        }

        for(int x = 1; x < sizeX - 1; x++)
        {
            tab[x][0].id = tab[x][sizeY - 2].id;
            tab[x][sizeY-1].id = tab[x][1].id;
        }

        tab[0][0].id = tab[sizeX-2][sizeY-2].id;
        tab[0][sizeY-1].id = tab[sizeX - 2][1].id;
        tab[sizeX-1][0].id = tab[1][sizeY-2].id;
        tab[sizeX-1][sizeY-1].id = tab[1][1].id;
    }


    //metody do pobierania danych z obrazu
    BufferedImage image;
    void getDimensions() {
        File imageFile = new File("mostek.png");
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.err.println("Blad odczytu obrazka");
            e.printStackTrace();
        }

        sizeX = image.getWidth();
        sizeY = image.getHeight();

        initializeArea();

    }
    void compArea() throws IOException //generacja modelu cyfrowej mikrostruktury w objętości komponentu o złożonym kształcie domeny obliczeniowej
    {
        for(int x=0; x<sizeX; x++)
        {
            for(int y=0; y<sizeY; y++)
            {
                int tmpColor = image.getRGB(x, y);

                if(tmpColor == Color.white.getRGB())
                {
                    tab[x][y].id = -1;
                }
            }
        }


        File pic=new File ("Nowy.bmp");
        ImageIO.write(image,"bmp",pic);

    }


    //funkcje: nadawanie koloru i generowanie obrazka
    int[][] colors;
    void colours() {
        Random random = new Random();

        colors[0][0]=0;
        colors[0][1]=0;
        colors[0][2]=0;

        for(int i =1;i<colors.length;i++){
            colors[i][0]=random.nextInt(255);
            colors[i][1]=random.nextInt(255);
            colors[i][2]=random.nextInt(255);

        }
    }
    void print() throws IOException {

        colors = new int[grainAmount+2][3];
        colours();
        BufferedImage bI = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        for (int k = 1; k < sizeX-1; k++)
        {
            for (int l = 1; l < sizeY-1; l++)
            {
                if(tab[k][l].id != -1)
                {
                    bI.setRGB(k, l, new Color((colors[tab[k][l].id][0]), (colors[tab[k][l].id][1]), (colors[tab[k][l].id][2])).getRGB());

                }

                else
                {
                    bI.setRGB(k, l, Color.white.getRGB());
                }
            }
            String s = "result"+ ".png";
            ImageIO.write(bI, "png", new File(s));
        }
    }


}
