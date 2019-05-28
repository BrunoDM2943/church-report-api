package com.bitshammer.reports;


import com.bitshammer.client.churchMembers.ChurchMembersAPI;
import com.bitshammer.client.churchMembers.MembersSearchResponse;
import com.bitshammer.reports.jasper.JasperService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
public class TestReportsService {

    @InjectMocks
    private ReportsService reportsService;

    @Mock
    private ChurchMembersAPI churchMembersAPI;

    @Mock
    private JasperService jasperService;

    @Test
    public void TestGenerateMembersReportOK() throws Exception {
        List<MembersSearchResponse> members = new ArrayList<>();
        IntStream.range(0, 100).forEach(i -> members.add(new MembersSearchResponse("Adulto",
                MembersSearchResponse.Pessoa.builder()
                        .nome("Teste")
                        .contato(new MembersSearchResponse.Pessoa.Contato("1112341234", "1112341234", "email@xicas.com"))
                        .endereco(new MembersSearchResponse.Pessoa.Endereco("Rua de teste"))
                        .dtNascimento(LocalDate.now())
                        .build())));
        Mockito.when(churchMembersAPI.getMembers()).thenReturn(members);
        Mockito.when(jasperService.generateReport(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenCallRealMethod();
        byte[] bytes = reportsService.generateMembersReport();
        Assert.assertNotEquals(0, bytes.length);
    }

}
