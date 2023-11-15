$('#modal-excluir').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	
	var codigoAssociado = button.data('id');
	var descricaoAssociado = button.data('nome');
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';
	}
	form.attr('action', action + codigoAssociado);
	
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o associado <strong>' + descricaoAssociado + '</strong>?');
});
