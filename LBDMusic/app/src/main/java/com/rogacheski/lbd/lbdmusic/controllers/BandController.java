package com.rogacheski.lbd.lbdmusic.controllers;

import android.content.Context;
import android.graphics.drawable.Drawable;


import com.rogacheski.lbd.lbdmusic.R;
import com.rogacheski.lbd.lbdmusic.entity.BandEntity;
import com.rogacheski.lbd.lbdmusic.entity.ContatoEntity;
import com.rogacheski.lbd.lbdmusic.entity.TagEntity;

import java.util.ArrayList;

/**
 * Created by vis_a on 28-Apr-17.
 */

public class BandController {


    public static BandEntity buscarBandaPorId(int id_banda, Context context) {
        BandEntity banda = new BandEntity();
        if(id_banda == 0) {

            banda.setsNomeBanda("Lumineers");
            banda.setsDescricaoBanda("The Lumineers are nominated for Top Rock Artist and Top " +
                    "Rock Album in this year’s Billboard Music Awards! " +
                    "Tune in to the show on May 21st on ABC!");
            //banda.setdImagemDescBanda(context.getDrawable(R.drawable.descimage));
            //banda.setdImagemBanda(context.getDrawable(R.drawable.mainimage));

            TagEntity tag1 = new TagEntity(5, "Folk Rock");
            TagEntity tag2 = new TagEntity(6, "Folk");
            TagEntity tag3 = new TagEntity(7, "Acoustic");
            TagEntity tag4 = new TagEntity(8, "Americana");
            ArrayList<TagEntity> tagList = new ArrayList<TagEntity>();
            tagList.add(tag1);
            tagList.add(tag2);
            tagList.add(tag3);
            tagList.add(tag4);

            banda.setTags(tagList);

            ContatoEntity contato1 = new ContatoEntity(5, "YouTube", "https://www.youtube.com/user/TheLumineers?&ab_channel=TheLumineers");
            ContatoEntity contato2 = new ContatoEntity(8, "Facebook", "https://www.facebook.com/TheLumineers/");
            ContatoEntity contato3 = new ContatoEntity(10, "SoundCloud", "https://soundcloud.com/thelumineers");
            ContatoEntity contato4 = new ContatoEntity(1, "Telefone", "55 11 9-6022-2134");

            ArrayList<ContatoEntity> listaContatos = new ArrayList<ContatoEntity>();
            listaContatos.add(contato1);
            listaContatos.add(contato2);
            listaContatos.add(contato3);
            listaContatos.add(contato4);
            banda.setAverageRating(5);
            banda.setListaContatos(listaContatos);

        }else {

            banda.setsNomeBanda("Metallica");
            banda.setsDescricaoBanda("Metallica is an American heavy metal band based in San Rafael, California. The band was formed in 1981 in Los Angeles when vocalist/guitarist " +
                    "James Hetfield responded to an advertisement posted by drummer Lars Ulrich in a local newspaper!");
            //banda.setdImagemDescBanda(context.getDrawable(R.drawable.metallica_live_la));
            //banda.setdImagemBanda(context.getDrawable(R.drawable.mettalica_princ));

            TagEntity tag1 = new TagEntity(5, "Hard Rock");
            TagEntity tag2 = new TagEntity(6, "Metal");
            TagEntity tag3 = new TagEntity(7, "Heavy Metal");
            ArrayList<TagEntity> tagList = new ArrayList<TagEntity>();
            tagList.add(tag1);
            tagList.add(tag2);
            tagList.add(tag3);

            banda.setTags(tagList);

            ContatoEntity contato1 = new ContatoEntity(5, "YouTube", "https://www.youtube.com/user/MetallicaTV?&ab_channel=MetallicaTV");
            ContatoEntity contato3 = new ContatoEntity(8, "Facebook", "https://www.facebook.com/Metallica/?rf=114342115249127");
            ContatoEntity contato4 = new ContatoEntity(1, "Telefone", "55 11 9-8291-2134");

            ArrayList<ContatoEntity> listaContatos = new ArrayList<ContatoEntity>();
            listaContatos.add(contato1);
            listaContatos.add(contato3);
            listaContatos.add(contato4);

            banda.setListaContatos(listaContatos);
            banda.setAverageRating(4.3);
        }




            return banda;
    }
}