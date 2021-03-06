package chinook.controller;

import java.io.Serializable;

import javax.ejb.EJBAccessException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import chinook.entity.Artist;
import chinook.service.ArtistService;

@Named
@ViewScoped
public class EditArtistController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ArtistService artistService;
	private int artistId;
	private Artist currentArtist;
	
	public void findArtistById() {
		if (artistId <= 0) {
			Messages.addGlobalError("Bad request. Please use a link within the system");
			return;
		}
		
		Artist queryResult = artistService.findOneById(artistId);
		if ( queryResult == null ) {
			Messages.addGlobalError("Bad request. Unknown artistId {0}", artistId);
			return;
		}
		
		currentArtist = queryResult;		
	}

	public String updateArtist() {
		String outcome = null;
		try {
			artistService.update(currentArtist);
			currentArtist = null;
			Messages.addFlashGlobalInfo("Updated was successful.");
			outcome = "/public/viewArtists?faces-redirect=true";
		} catch (EJBAccessException e) {
			Messages.addGlobalError("You do not have permission to update this item.");
		} catch (EJBTransactionRolledbackException e) {
			Messages.addGlobalError("This item cannot be updated.");
		} catch (Exception e) {
			Messages.addGlobalError("Update was not successful.");
		}
		return outcome;
	}
	
	public String removeArtist() {
		String outcome = null;
		try {
			artistService.delete(currentArtist);
			currentArtist = null;
			Messages.addFlashGlobalInfo("Delete was successful.");
			outcome = "/public/viewArtists?faces-redirect=true";
		} catch (EJBAccessException e) {
			Messages.addGlobalError("You do not have permission to delete this item.");
		} catch (EJBTransactionRolledbackException e) {
			Messages.addGlobalError("This item cannot be deleted.");
		} catch (Exception e) {
			Messages.addGlobalError("Delete was not successful.");
		}
		return outcome;
	}
	
	public String cancel() {
		currentArtist = null;
		artistId = 0;
		return "/public/viewArtists?faces-redirect=true";
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public Artist getCurrentArtist() {
		return currentArtist;
	}

	public void setCurrentArtist(Artist currentArtist) {
		this.currentArtist = currentArtist;
	}
	
}
