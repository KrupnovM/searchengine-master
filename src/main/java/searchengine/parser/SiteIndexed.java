package searchengine.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import searchengine.config.SitesList;
import searchengine.dto.statistics.IndexDto;
import searchengine.dto.statistics.LemmaDto;
import searchengine.dto.statistics.PageDto;
import searchengine.model.*;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@Slf4j
public class SiteIndexed implements Runnable {

    private static final int processorCoreCount = Runtime.getRuntime().availableProcessors();
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;
    private final LemmaParser lemmaParser;
    private final IndexParser indexParser;
    private final String url;
    private final SitesList sitesList;


    @Override
    public void run() {
        log.info("Начата индексация - " + url + " " + getName());
        saveDateSite();
        try {
            List<PageDto> pageDtoList = getPageDtoList();
            saveToBase(pageDtoList);
            getLemmasPage();
            indexingWords();

        } catch (InterruptedException e) {
            log.error("Индексация остановлена - " + url);
            errorSite();
        }
    }

    private List<PageDto> getPageDtoList() throws InterruptedException {
        if (!Thread.interrupted()) {
            String urlFormat = url + "/";
            List<PageDto> pageDtoVector = new Vector<>();
            List<String> urlList = new Vector<>();
            ForkJoinPool forkJoinPool = new ForkJoinPool(processorCoreCount);
            List<PageDto> pages = (List<PageDto>) forkJoinPool.invoke(new PageUrlParser(urlFormat, pageDtoVector, urlList));
            return new CopyOnWriteArrayList<>(pages);
        } else throw new InterruptedException();
    }

    private void saveToBase(List<PageDto> pages) throws InterruptedException {
        if (!Thread.interrupted()) {
            Site site = siteRepository.findByUrl(url);
            List<Page> pageList = new ArrayList<>(pages.size());

            for (PageDto page : pages) {
                int start = page.getUrl().indexOf(url) + url.length();
                String pageFormat = page.getUrl().substring(start);
                pageList.add(new Page(site, pageFormat, page.getCode(), page.getContent()));
            }

            pageRepository.saveAll(pageList);
            pageRepository.flush();
        } else {
            throw new InterruptedException();
        }
    }

    private void getLemmasPage() {
        if (!Thread.interrupted()) {
            Site siteEntity = siteRepository.findByUrl(url);
            siteEntity.setStatusTime(new Date());
            lemmaParser.run(siteEntity);
            List<LemmaDto> lemmaDtoList = lemmaParser.getLemmaDtoList();
            List<Lemma> lemmaList = new ArrayList<>(lemmaDtoList.size());

            for (LemmaDto lemmaDto : lemmaDtoList) {
                lemmaList.add(new Lemma(lemmaDto.getLemma(), lemmaDto.getFrequency(), siteEntity));
            }

            lemmaRepository.saveAll(lemmaList);
            lemmaRepository.flush();
        } else {
            throw new RuntimeException();
        }
    }

    private void indexingWords() throws InterruptedException {
        if (!Thread.interrupted()) {
            Site site = siteRepository.findByUrl(url);
            indexParser.run(site);
            List<IndexDto> indexDtoList = indexParser.getIndexList();
            List<Index> indexList = new ArrayList<>(indexDtoList.size());
            site.setStatusTime(new Date());

            for (IndexDto indexDto : indexDtoList) {
                Page page = pageRepository.getById(indexDto.getPageID());
                Lemma lemma = lemmaRepository.getById(indexDto.getLemmaID());
                indexList.add(new Index(page, lemma, indexDto.getRank()));
            }

            indexRepository.saveAll(indexList);
            indexRepository.flush();
            log.info("Индексация завершена - " + url);
            site.setStatusTime(new Date());
            site.setStatus(Status.INDEXED);
            siteRepository.save(site);

        } else {
            throw new InterruptedException();
        }
    }

    private void deleteDataFromSite() {
        Site site = siteRepository.findByUrl(url);
        site.setStatus(Status.INDEXING);
        site.setName(getName());
        site.setStatusTime(new Date());
        siteRepository.save(site);
        siteRepository.flush();
        siteRepository.delete(site);
    }

    private void saveDateSite() {
        Site site = new Site();
        site.setUrl(url);
        site.setName(getName());
        site.setStatus(Status.INDEXING);
        site.setStatusTime(new Date());
        siteRepository.flush();
        siteRepository.save(site);
    }

    private void errorSite() {
        Site site = new Site();
        site.setLastError("Индексация остановлена");
        site.setStatus(Status.FAILED);
        site.setStatusTime(new Date());
        siteRepository.save(site);
    }

    private String getName() {
        List<searchengine.config.Site> sites = sitesList.getSites();
        for (searchengine.config.Site map : sites) {
            if (map.getUrl().equals(url)) {
                return map.getName();
            }
        }
        return "";
    }
}

