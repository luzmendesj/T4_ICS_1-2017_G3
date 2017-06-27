
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class notes {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    

    public static void main(String[] args) throws Exception {
    	
        Sequence sequence = MidiSystem.getSequence(new File("test2.mid"));
        File arquivo = new File( "carta.txt" );
        arquivo.createNewFile();
        FileWriter fw = new FileWriter( arquivo );
        BufferedWriter bw = new BufferedWriter( fw );
        

        int trackNumber = 0;
        int flag = 0;
        long tickAtual = 0;
        long tickAux = 0;
        long duracao = 0;
        
        //pega as trilhas
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            
            bw.newLine();
            bw.newLine();
            bw.newLine();
          
            System.out.println();
            for (int i=0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
               
                	tickAtual = event.getTick();
                
                if (flag == 0) {
          
                	bw.write( "instrumento" + trackNumber + " ");
                	bw.write(tickAtual + " ");
                	
            }
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                        tickAux = tickAtual;
                        flag = 1;
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                        duracao = tickAtual - tickAux;
                    
                       bw.write(duracao + " ");
                        bw.newLine();
                        flag = 0;
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
        }
        
        bw.close();
        fw.close();

    }
}

