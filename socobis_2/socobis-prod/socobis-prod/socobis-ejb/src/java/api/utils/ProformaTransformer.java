// package api.utils;

// import proforma.ProformaLib;
// import api.dto.ProformaListeDTO;
// import java.util.ArrayList;
// import java.util.List;
// import bean.CGenUtil;
// import bean.ClassMAPTable;

// public class ProformaTransformer {
    
//     public static ProformaListeDTO[] transformToDTO(ProformaLib[] proformaLibs) throws Exception {
//         if (proformaLibs == null) {
//             return new ProformaListeDTO[0];
//         }
        
//         List<ProformaListeDTO> dtoList = new ArrayList<>();
        
//         for (ProformaLib proformaLib : proformaLibs) {
//             ProformaListeDTO dto = transformSingleToDTO(proformaLib);
//             dtoList.add(dto);
//         }
        
//         return dtoList.toArray(new ProformaListeDTO[0]);
//     }
    

//     public static ProformaListeDTO transformSingleToDTO(ProformaLib proformaLib) throws Exception {
//         ProformaListeDTO dto = new ProformaListeDTO();
        
//         // Transformation des champs de base
//         dto.setId(proformaLib.getId());
//         dto.setDesignation(proformaLib.getDesignation());
//         dto.setIdMagasin(proformaLib.getIdMagasin());
//         dto.setIdMagasinLib(proformaLib.getIdMagasinLib());
//         dto.setDaty(proformaLib.getDaty());
//         dto.setRemarque(proformaLib.getRemarque());
//         dto.setEtat(proformaLib.getEtat());
//         dto.setEtatLib(proformaLib.getEtatLib());
//         dto.setIdDevise(proformaLib.getIdDevise());
//         dto.setIdClient(proformaLib.getIdClient());
//         dto.setIdClientLib(proformaLib.getIdClientLib());
//         dto.setAdresse(proformaLib.getAdresse());
//         dto.setContact(proformaLib.getContact());
        
//         // Transformation des montants
//         dto.setMontant(proformaLib.getMontant());
//         dto.setMontantTotal(proformaLib.getMontantTotal());
//         dto.setMontantRemise(proformaLib.getMontantremise());
//         dto.setMontantTva(proformaLib.getMontantTva());
//         dto.setMontantTtc(proformaLib.getMontantTtc());
//         dto.setMontantTtcAr(proformaLib.getMontantTtcAr());
//         dto.setMontantPaye(proformaLib.getMontantPaye());
//         dto.setMontantReste(proformaLib.getMontantreste());
//         dto.setAvoir(proformaLib.getAvoir());
//         dto.setTauxDechange(proformaLib.getTauxDechange());
//         dto.setMontantRevient(proformaLib.getMontantRevient());
//         dto.setMargeBrute(proformaLib.getMargeBrute());
        
//         // Transformation des autres champs
//         dto.setIdReservation(proformaLib.getIdReservation());
//         dto.setIdOrigine(proformaLib.getIdOrigine());
//         dto.setRemise(proformaLib.getRemise());
//         dto.setDateDebutMin(proformaLib.getDatedebutmin());
//         dto.setDateFinMax(proformaLib.getDatefinmax());
//         dto.setPeriode(proformaLib.getPeriode());
        
//         // État de paiement (sera calculé automatiquement via setMontantReste)
//         dto.setEtatPaymentLib(proformaLib.getEtatpaymentlib());
        
//         return dto;
//     }
    

//     public static ProformaListeDTO[] getProformasAfter2026() throws Exception {
//         String query = "SELECT * FROM PROFORMA_CPL WHERE DATY > DATE '2026-01-01'";
        
//         ProformaLib[] proformaLibs = (ProformaLib[]) CGenUtil.rechercher(
//             (ClassMAPTable) new ProformaLib(), query);
//         return transformToDTO(proformaLibs);
//     }
// }